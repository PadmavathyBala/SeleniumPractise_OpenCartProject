/**
 * Dashboard view: KPIs, filters, and the flight table.
 */
const Dashboard = (() => {

    let allFlights = [];
    let etdModal;

    function init() {
        etdModal = new bootstrap.Modal(document.getElementById('etdModal'));

        document.getElementById('searchInput')
            .addEventListener('input', debounce(refresh, 300));
        document.getElementById('statusFilter')
            .addEventListener('change', render);
        document.getElementById('sortBy')
            .addEventListener('change', render);
        document.getElementById('refreshBtn')
            .addEventListener('click', refresh);
        document.getElementById('modalSaveBtn')
            .addEventListener('click', saveEtdUpdate);
    }

    async function refresh() {
        try {
            const search = document.getElementById('searchInput').value.trim();
            allFlights = await API.listFlights(search);
            const stats = await API.getStatistics();
            updateKpis(stats);
            render();
            updateLastUpdated();
        } catch (err) {
            App.toast('Failed to load flights: ' + err.message, 'danger');
        }
    }

    function updateKpis(stats) {
        document.getElementById('kpiTotal').textContent = stats.totalFlights;
        document.getElementById('kpiOnTime').textContent = stats.onTimeCount;
        document.getElementById('kpiOnTimePct').textContent =
            stats.onTimePercentage.toFixed(1) + '% of total';
        document.getElementById('kpiDelayed').textContent = stats.delayedCount;
        document.getElementById('kpiAvgDelay').textContent =
            stats.averageDelayMinutes > 0
                ? `Avg ${stats.averageDelayMinutes} min`
                : 'No delays';
        document.getElementById('kpiCancelled').textContent = stats.cancelledCount;
    }

    function render() {
        const statusFilter = document.getElementById('statusFilter').value;
        const sortBy = document.getElementById('sortBy').value;

        let flights = [...allFlights];

        if (statusFilter) {
            flights = flights.filter(f => f.status === statusFilter);
        }

        flights.sort((a, b) => {
            switch (sortBy) {
                case 'scheduled':
                    return new Date(a.scheduledDeparture) - new Date(b.scheduledDeparture);
                case 'delay':
                    return b.delayMinutes - a.delayMinutes;
                case 'flight':
                    return a.flightNumber.localeCompare(b.flightNumber);
                case 'etd':
                default:
                    return new Date(a.estimatedDeparture) - new Date(b.estimatedDeparture);
            }
        });

        const tbody = document.getElementById('flightsTableBody');
        if (flights.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="text-center text-muted py-4">No flights match the current filters.</td></tr>';
            return;
        }

        tbody.innerHTML = flights.map(renderRow).join('');
        attachRowHandlers();
    }

    function renderRow(f) {
        const rowClass = f.status === 'CANCELLED' ? 'row-cancelled'
                      : f.status === 'DEPARTED'  ? 'row-departed'
                      : f.delayed                ? 'row-delayed' : '';

        const delayClass = f.delayMinutes === 0 ? 'delay-zero'
                        : f.delayMinutes < 30  ? 'delay-minor'
                                               : 'delay-major';

        return `
        <tr class="${rowClass}" data-id="${f.id}">
            <td><span class="flight-number">${f.flightNumber}</span></td>
            <td>${escapeHtml(f.airline || '')}</td>
            <td class="route">
                <span class="iata">${f.origin}</span>
                <i class="bi bi-arrow-right"></i>
                <span class="iata">${f.destination}</span>
            </td>
            <td>${escapeHtml(f.gate || '—')}</td>
            <td>${formatTime(f.scheduledDeparture)}</td>
            <td>${formatTime(f.estimatedDeparture)}</td>
            <td><span class="delay-badge ${delayClass}">
                ${f.delayMinutes > 0 ? '+' + f.delayMinutes + 'm' : 'on time'}
            </span></td>
            <td><span class="badge status-badge status-${f.status}">${f.statusDisplay}</span></td>
            <td>${f.delayReasonDisplay || '—'}</td>
            <td class="text-center">
                <button class="btn btn-sm btn-outline-primary btn-action" data-action="edit"
                        title="Update ETD">
                    <i class="bi bi-clock"></i>
                </button>
                <button class="btn btn-sm btn-outline-success btn-action" data-action="depart"
                        title="Mark Departed">
                    <i class="bi bi-airplane"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger btn-action" data-action="cancel"
                        title="Cancel Flight">
                    <i class="bi bi-x-octagon"></i>
                </button>
                <button class="btn btn-sm btn-outline-secondary btn-action" data-action="delete"
                        title="Delete">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        </tr>`;
    }

    function attachRowHandlers() {
        document.querySelectorAll('#flightsTableBody [data-action]').forEach(btn => {
            btn.addEventListener('click', async (e) => {
                const tr = e.target.closest('tr');
                const id = tr.dataset.id;
                const action = btn.dataset.action;
                const flight = allFlights.find(f => f.id === id);

                try {
                    if (action === 'edit') {
                        openEtdModal(flight);
                    } else if (action === 'depart') {
                        if (!confirm(`Mark ${flight.flightNumber} as departed?`)) return;
                        await API.departFlight(id);
                        App.toast(`${flight.flightNumber} marked departed`, 'success');
                        refresh();
                    } else if (action === 'cancel') {
                        const notes = prompt(`Cancellation note for ${flight.flightNumber}:`, '');
                        if (notes === null) return;
                        await API.cancelFlight(id, notes);
                        App.toast(`${flight.flightNumber} cancelled`, 'warning');
                        refresh();
                    } else if (action === 'delete') {
                        if (!confirm(`Permanently delete ${flight.flightNumber}?`)) return;
                        await API.deleteFlight(id);
                        App.toast(`${flight.flightNumber} deleted`, 'secondary');
                        refresh();
                    }
                } catch (err) {
                    App.toast('Action failed: ' + err.message, 'danger');
                }
            });
        });
    }

    function openEtdModal(flight) {
        document.getElementById('modalFlightId').value = flight.id;
        document.getElementById('modalFlightNumber').textContent = flight.flightNumber;
        document.getElementById('modalFlightRoute').textContent =
            `${flight.origin} → ${flight.destination}`;
        document.getElementById('modalNewEtd').value =
            toLocalDateTimeInput(flight.estimatedDeparture);
        document.getElementById('modalReason').value = flight.delayReason || '';
        document.getElementById('modalNotes').value = flight.delayNotes || '';
        etdModal.show();
    }

    async function saveEtdUpdate() {
        const id = document.getElementById('modalFlightId').value;
        const newEtd = document.getElementById('modalNewEtd').value;
        const reason = document.getElementById('modalReason').value;
        const notes  = document.getElementById('modalNotes').value;

        if (!newEtd) {
            App.toast('Please pick a new ETD', 'warning');
            return;
        }

        try {
            await API.updateEtd(id, {
                newEstimatedDeparture: newEtd,
                delayReason: reason || null,
                delayNotes: notes || null
            });
            etdModal.hide();
            App.toast('ETD updated', 'success');
            refresh();
        } catch (err) {
            App.toast('Update failed: ' + err.message, 'danger');
        }
    }

    function updateLastUpdated() {
        document.getElementById('lastUpdate').textContent =
            new Date().toLocaleTimeString();
    }

    function formatTime(iso) {
        if (!iso) return '—';
        const d = new Date(iso);
        return d.toLocaleString([], {
            month: 'short', day: 'numeric',
            hour: '2-digit', minute: '2-digit', hour12: false
        });
    }

    function toLocalDateTimeInput(iso) {
        if (!iso) return '';
        const d = new Date(iso);
        const pad = (n) => String(n).padStart(2, '0');
        return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}` +
               `T${pad(d.getHours())}:${pad(d.getMinutes())}`;
    }

    function escapeHtml(s) {
        return String(s).replace(/[&<>"']/g, c =>
            ({ '&':'&amp;', '<':'&lt;', '>':'&gt;', '"':'&quot;', "'":'&#39;' }[c]));
    }

    function debounce(fn, ms) {
        let t;
        return (...args) => {
            clearTimeout(t);
            t = setTimeout(() => fn(...args), ms);
        };
    }

    return { init, refresh };
})();
