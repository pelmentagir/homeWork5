document.getElementById("addIngredient").addEventListener("click", function () {
    const ingredientsDiv = document.getElementById("ingredients");

    const ingredientCount = ingredientsDiv.getElementsByClassName("ingredient").length;

    const newIngredientDiv = document.createElement("div");
    newIngredientDiv.classList.add("ingredient", "form-group");

    newIngredientDiv.innerHTML = `
            <input type="text" name="ingredientName[]" class="input-field" placeholder="Ingredient Name" required>
            <input type="number" name="quantity[]" class="input-field" min="1" step="0.01" placeholder="Quantity" required>
            <input type="text" name="unit[]" class="input-field" placeholder="Unit (e.g., g, ml, tsp)" required>
        `;

    ingredientsDiv.appendChild(newIngredientDiv);
});

document.getElementById('uploadPhotoButton').addEventListener('click', function (event) {
    event.preventDefault();

    const form = document.getElementById('uploadPhotoForm');
    const formData = new FormData(form);

    fetch(`${form.action}`, {
        method: 'POST',
        body: formData,
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('photoUrl').value = data.photoUrl;
                alert('Фото успешно загружено!');
            } else {
                alert('Ошибка загрузки фото: ' + data.error);
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Ошибка загрузки фото.');
        });
});