// Theme management
(function() {
    const THEME_KEY = 'app-theme';
    const DARK_THEME = 'dark';
    const LIGHT_THEME = 'light';

    function initTheme() {
        // Dark-first: if user hasn't chosen, default to dark
        const savedTheme = localStorage.getItem(THEME_KEY);
        const initialTheme = savedTheme || DARK_THEME;
        applyTheme(initialTheme);
        updateThemeToggle(initialTheme);
    }

    function applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem(THEME_KEY, theme);
    }

    function toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme') || LIGHT_THEME;
        const newTheme = currentTheme === DARK_THEME ? LIGHT_THEME : DARK_THEME;
        applyTheme(newTheme);
        updateThemeToggle(newTheme);
    }

    function updateThemeToggle(theme) {
        const toggleBtn = document.getElementById('themeToggle');
        if (toggleBtn) {
            const icon = toggleBtn.querySelector('i');
            if (icon) {
                icon.className = theme === DARK_THEME 
                    ? 'bi bi-sun-fill' 
                    : 'bi bi-moon-fill';
            }
            toggleBtn.setAttribute('title', theme === DARK_THEME ? 'Switch to Light Mode' : 'Switch to Dark Mode');
        }
    }

    // Initialize theme on page load
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initTheme);
    } else {
        initTheme();
    }

    // Expose toggle function globally
    window.toggleTheme = toggleTheme;
})();
