/**
 * Thin REST client for the ETD backend.
 */
const API = (() => {
    const BASE = '/api';

    async function request(path, options = {}) {
        const res = await fetch(BASE + path, {
            headers: { 'Content-Type': 'application/json' },
            ...options
        });
        if (!res.ok) {
            let msg = `HTTP ${res.status}`;
            try {
                const err = await res.json();
                msg = err.message || msg;
                if (err.fieldErrors) {
                    msg += ': ' + Object.entries(err.fieldErrors)
                        .map(([k, v]) => `${k} — ${v}`).join('; ');
                }
            } catch (_) {}
            throw new Error(msg);
        }
        if (res.status === 204) return null;
        return res.json();
    }

    return {
        listFlights:    (search) => request('/flights' + (search ? `?search=${encodeURIComponent(search)}` : '')),
        getFlight:      (id)     => request(`/flights/${id}`),
        createFlight:   (body)   => request('/flights', { method: 'POST', body: JSON.stringify(body) }),
        updateEtd:      (id, b)  => request(`/flights/${id}/etd`, { method: 'PUT',  body: JSON.stringify(b) }),
        cancelFlight:   (id, n)  => request(`/flights/${id}/cancel`, { method: 'POST', body: JSON.stringify({ notes: n }) }),
        departFlight:   (id)     => request(`/flights/${id}/depart`, { method: 'POST' }),
        deleteFlight:   (id)     => request(`/flights/${id}`, { method: 'DELETE' }),
        getStatistics:  ()       => request('/statistics')
    };
})();
