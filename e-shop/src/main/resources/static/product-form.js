document.addEventListener('DOMContentLoaded', function () {
  // znajdź input (po id lub name)
  const fileInput = document.getElementById('image') || document.querySelector('input[name="imageFile"]');
  const preview = document.getElementById('imagePreview');
  const placeholder = document.getElementById('noImagePlaceholder');

  if (!fileInput) {
    console.warn('No file input found for image preview.');
    return;
  }

  // zapamiętaj oryginalne src (może być pusty)
  const originalSrc = preview ? preview.getAttribute('src') : null;

  fileInput.addEventListener('change', function () {
    const file = this.files && this.files[0];
    if (!file) {
      // przywróć oryginalny stan
      if (preview) {
        if (originalSrc && originalSrc.trim() !== '') {
          preview.src = originalSrc;
          preview.classList.remove('d-none');
          if (placeholder) placeholder.classList.add('d-none');
        } else {
          preview.classList.add('d-none');
          if (placeholder) placeholder.classList.remove('d-none');
        }
      }
      return;
    }

    if (!file.type.startsWith('image/')) {
      alert('Please select an image file.');
      this.value = '';
      return;
    }

    const reader = new FileReader();
    reader.onload = function (e) {
      if (preview) {
        preview.src = e.target.result;
        preview.classList.remove('d-none');
      }
      if (placeholder) placeholder.classList.add('d-none');
    };
    reader.readAsDataURL(file);
  });
});



