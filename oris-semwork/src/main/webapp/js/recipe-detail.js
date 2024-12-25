function goBack() {
    window.history.back();
}

document.addEventListener('DOMContentLoaded', () => {
    const stars = document.querySelectorAll('.star');
    const ratingValueInput = document.getElementById('ratingValue');

    stars.forEach(star => {
        star.addEventListener('click', () => {
            const rating = star.getAttribute('data-value');
            ratingValueInput.value = rating;
            updateStarSelection(rating);
        });

        star.addEventListener('mouseover', () => {
            const hoverValue = star.getAttribute('data-value');
            updateStarSelection(hoverValue);
        });

        star.addEventListener('mouseout', () => {
            updateStarSelection(ratingValueInput.value || 0);
        });
    });

    function updateStarSelection(value) {
        stars.forEach(star => {
            if (parseInt(star.getAttribute('data-value')) <= parseInt(value)) {
                star.classList.add('selected');
            } else {
                star.classList.remove('selected');
            }
        });
    }
});