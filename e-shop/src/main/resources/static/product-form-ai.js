function generateField(textareaId, url) {
    // Pobieranie nazwy produktu
    const productNameInput = document.querySelector('input[name="name"]');
    const productName = productNameInput ? productNameInput.value : '';
    
    // Pobieranie nazwy kategorii - FIXED LINE
    const categorySelect = document.querySelector('select[name="categoryName"]');
    const categoryName = categorySelect ? categorySelect.value : '';

    console.log('Product Name:', productName);
    console.log('Category Name:', categoryName);
    console.log('Textarea ID:', textareaId);
    console.log('URL:', url);

    if (!productName.trim()) {
        alert('Please enter a product name first');
        if (productNameInput) {
            productNameInput.focus();
        }
        return;
    }

    if (!categoryName.trim()) {
        alert('Please select a category first');
        if (categorySelect) {
            categorySelect.focus();
        }
        return;
    }

    const button = event.target;
    const originalText = button.innerHTML;

    button.innerHTML = '<i class="bi bi-hourglass-split me-1"></i> Generating...';
    button.disabled = true;
    button.classList.add('disabled');

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: new URLSearchParams({
            productName: productName,
            categoryName: categoryName
        })
    })
    .then(response => {
        console.log('Response status:', response.status);
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || `HTTP error! status: ${response.status}`);
            });
        }
        return response.text();
    })
    .then(data => {
        console.log('Generated data:', data);
        document.getElementById(textareaId).value = data;
        showNotification('Content generated successfully!', 'success');
    })
    .catch(err => {
        console.error('Error:', err);
        showNotification('Error generating content: ' + err.message, 'error');
    })
    .finally(() => {
        button.innerHTML = originalText;
        button.disabled = false;
        button.classList.remove('disabled');
    });
}