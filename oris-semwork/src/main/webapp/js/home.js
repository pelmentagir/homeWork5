document.addEventListener("DOMContentLoaded", () => {
    const categoryItem = document.querySelector(".category-item");
    const subcategories = document.querySelector(".subcategories");
    let hideTimeout;

    categoryItem.addEventListener("mouseenter", () => {
    clearTimeout(hideTimeout);
    subcategories.style.display = "block";
});

    categoryItem.addEventListener("mouseleave", () => {
    hideTimeout = setTimeout(() => {
    subcategories.style.display = "none";
}, 300);
});

    subcategories.addEventListener("mouseenter", () => {
    clearTimeout(hideTimeout);
});

    subcategories.addEventListener("mouseleave", () => {
    hideTimeout = setTimeout(() => {
    subcategories.style.display = "none";
}, 300);
});
});
