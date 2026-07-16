/**
 * Application entry point: wires up modules and starts auto-refresh.
 */
const App = (() => {

    const REFRESH_INTERVAL_MS = 15000;
    let refreshTimer;

    function init() {
        Dashboard.init();
        Statistics.init();
        AddFlight.init();

        Dashboard.refresh();
        startAutoRefresh();
    }

    function startAutoRefresh() {
        if (refreshTimer) clearInterval(refreshTimer);
        refreshTimer = setInterval(() => {
            // Only refresh the visible tab
            const activeTab = document.querySelector('#mainTabs .nav-link.active');
            const target = activeTab?.dataset.bsTarget;
            if (target === '#dashboard') {
                Dashboard.refresh();
            } else if (target === '#statistics') {
                Statistics.refresh();
                Dashboard.refresh();  // keep KPI/cache fresh in background
            }
        }, REFRESH_INTERVAL_MS);
    }

    /**
     * Show a toast notification. Variant maps to Bootstrap color (success, danger, ...).
     */
    function toast(message, variant = 'primary') {
        const id = 'toast-' + Date.now();
        const container = document.getElementById('toastContainer');
        const html = `
            <div id="${id}" class="toast align-items-center text-bg-${variant} border-0"
                 role="alert">
                <div class="d-flex">
                    <div class="toast-body">${message}</div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto"
                            data-bs-dismiss="toast"></button>
                </div>
            </div>`;
        container.insertAdjacentHTML('beforeend', html);
        const el = document.getElementById(id);
        const t = new bootstrap.Toast(el, { delay: 3500 });
        t.show();
        el.addEventListener('hidden.bs.toast', () => el.remove());
    }

    return { init, toast };
})();

document.addEventListener('DOMContentLoaded', App.init);
