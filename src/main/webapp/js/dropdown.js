/**
 * Dropdown menu functionality
 */
document.addEventListener('DOMContentLoaded', function() {
    // Get all user menu elements
    const userMenus = document.querySelectorAll('.user-menu');

    // Add click event listener to each user menu
    userMenus.forEach(menu => {
        const menuLink = menu.querySelector('a');
        const dropdown = menu.querySelector('.dropdown');

        menuLink.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation(); // Prevent event bubbling

            // Toggle active class on the user menu
            const isActive = menu.classList.toggle('active');

            // Explicitly toggle dropdown visibility
            if (dropdown) {
                console.log('Toggle dropdown:', isActive ? 'show' : 'hide');
                if (isActive) {
                    dropdown.style.display = 'block';
                    console.log('Dropdown should be visible now');
                } else {
                    dropdown.style.display = 'none';
                    console.log('Dropdown should be hidden now');
                }
            }

            // Close other dropdowns
            userMenus.forEach(otherMenu => {
                if (otherMenu !== menu) {
                    otherMenu.classList.remove('active');
                    const otherDropdown = otherMenu.querySelector('.dropdown');
                    if (otherDropdown) {
                        otherDropdown.style.display = 'none';
                    }
                }
            });

            // Close dropdown when clicking outside
            document.addEventListener('click', function closeDropdown(event) {
                if (!menu.contains(event.target)) {
                    menu.classList.remove('active');
                    if (dropdown) {
                        dropdown.style.display = 'none';
                    }
                    document.removeEventListener('click', closeDropdown);
                }
            });
        });
    });
});
