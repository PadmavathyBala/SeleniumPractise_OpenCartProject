/**
 * Add Flight tab: form handling and validation.
 */
const AddFlight = (() => {

    function init() {
        document.getElementById('submitFlightBtn').addEventListener('click', submit);
        document.getElementById('resetFlightBtn').addEventListener('click', resetForm);

        // Default scheduled departure: 1 hour from now
        const now = new Date(Date.now() + 60 * 60 * 1000);
        const pad = (n) => String(n).padStart(2, '0');
        document.getElementById('newScheduledDeparture').value =
            `${now.getFullYear()}-${pad(now.getMonth()+1)}-${pad(now.getDate())}` +
            `T${pad(now.getHours())}:${pad(now.getMinutes())}`;
    }

    async function submit() {
        const body = {
            flightNumber: document.getElementById('newFlightNumber').value.trim().toUpperCase(),
            airline:      document.getElementById('newAirline').value.trim(),
            origin:       document.getElementById('newOrigin').value.trim().toUpperCase(),
            destination:  document.getElementById('newDestination').value.trim().toUpperCase(),
            gate:         document.getElementById('newGate').value.trim(),
            aircraftType: document.getElementById('newAircraftType').value.trim(),
            scheduledDeparture: document.getElementById('newScheduledDeparture').value
        };

        const alertBox = document.getElementById('addFlightAlert');
        alertBox.innerHTML = '';

        try {
            const created = await API.createFlight(body);
            alertBox.innerHTML = `
                <div class="alert alert-success alert-dismissible fade show">
                    Flight <strong>${created.flightNumber}</strong> created.
                    <button class="btn-close" data-bs-dismiss="alert"></button>
                </div>`;
            resetForm(false);
            App.toast(`Created flight ${created.flightNumber}`, 'success');
            Dashboard.refresh();
        } catch (err) {
            alertBox.innerHTML = `
                <div class="alert alert-danger alert-dismissible fade show">
                    ${err.message}
                    <button class="btn-close" data-bs-dismiss="alert"></button>
                </div>`;
        }
    }

    function resetForm(clearAlert = true) {
        ['newFlightNumber','newAirline','newOrigin','newDestination',
         'newGate','newAircraftType'].forEach(id => {
            document.getElementById(id).value = '';
        });
        if (clearAlert) document.getElementById('addFlightAlert').innerHTML = '';
        // Reset default time
        init();
    }

    return { init };
})();
