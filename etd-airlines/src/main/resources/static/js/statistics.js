/**
 * Statistics view: charts driven by Chart.js.
 */
const Statistics = (() => {

    let statusChart, reasonChart, airlineChart;

    function init() {
        // Charts created lazily when tab is first shown
        document.querySelector('[data-bs-target="#statistics"]')
            .addEventListener('shown.bs.tab', refresh);
    }

    async function refresh() {
        try {
            const stats = await API.getStatistics();
            renderStatusChart(stats.countByStatus);
            renderReasonChart(stats.countByDelayReason);
            renderAirlineChart(stats.countByAirline);
        } catch (err) {
            App.toast('Failed to load statistics: ' + err.message, 'danger');
        }
    }

    function renderStatusChart(data) {
        const labels = Object.keys(data);
        const values = Object.values(data);
        const colors = {
            SCHEDULED: '#6c757d', BOARDING: '#0dcaf0', DEPARTED: '#198754',
            DELAYED:   '#fd7e14', CANCELLED:'#dc3545', DIVERTED: '#6610f2'
        };
        const bgColors = labels.map(l => colors[l] || '#999');

        const ctx = document.getElementById('statusChart');
        if (statusChart) statusChart.destroy();
        statusChart = new Chart(ctx, {
            type: 'doughnut',
            data: { labels, datasets: [{ data: values, backgroundColor: bgColors }] },
            options: { responsive: true, plugins: { legend: { position: 'right' } } }
        });
    }

    function renderReasonChart(data) {
        const entries = Object.entries(data || {});
        if (entries.length === 0) {
            const ctx = document.getElementById('reasonChart');
            const parent = ctx.parentElement;
            parent.innerHTML = '<p class="text-muted text-center my-5">No delay reasons recorded yet.</p>';
            return;
        }
        const labels = entries.map(([k]) => k.replace(/_/g, ' '));
        const values = entries.map(([, v]) => v);

        const ctx = document.getElementById('reasonChart');
        if (reasonChart) reasonChart.destroy();
        reasonChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: 'Flights',
                    data: values,
                    backgroundColor: '#fd7e14'
                }]
            },
            options: {
                responsive: true,
                plugins: { legend: { display: false } },
                scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
            }
        });
    }

    function renderAirlineChart(data) {
        const entries = Object.entries(data || {});
        const labels = entries.map(([k]) => k);
        const values = entries.map(([, v]) => v);

        const ctx = document.getElementById('airlineChart');
        if (airlineChart) airlineChart.destroy();
        airlineChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: 'Flights',
                    data: values,
                    backgroundColor: '#0d6efd'
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                plugins: { legend: { display: false } },
                scales: { x: { beginAtZero: true, ticks: { stepSize: 1 } } }
            }
        });
    }

    return { init, refresh };
})();
