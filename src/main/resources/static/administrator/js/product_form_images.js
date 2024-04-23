var extraImagesCount = 0;
$(document).ready(function() {

    $("input[name='extraImage']").each(function(index) {
        extraImagesCount++;

        $(this).change(function() {
            if (!checkFileSize(this)) {
                return;
            }
            showExtraImageThumbnail(this, index);
        });
    });

    $("a[name='linkRemoveExtraImage']").each(function(index) {
        $(this).click(function() {
            removeExtraImage(index);
        });
    });

});

// hiển thị ExtraImage
function showExtraImageThumbnail(fileInput, index) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function(e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);

    if (index >= extraImagesCount - 1) {
        addNextExtraImageSection(index + 1);
    }
}


// thêm ExtraImage tiếp theo
function addNextExtraImageSection(index) {
    htmlExtraImage = `
		<div class="col-md-4 col-sm-12  border  p-3 " id="divExtraImage${index}">
			<div id="extraImageHeader${index}"><label class="form-label">Ảnh phụ #${index + 1}:</label></div>
			<div class="m-2">
                <input type="file" class="form-control"  accept="image/png, image/jpeg" 
                        name="extraImage"
                        onchange="showExtraImageThumbnail(this, ${index})" />
			</div>
			<div>
				<img alt="Extra image #${index + 1} preview" class="img-fluid" style="width: 250px; height: auto; padding: 10px 0px;"
				    id="extraThumbnail${index}"
					src="${defaultImageThumbnailSrc}"/>
			</div>
		</div>	
	`;

    htmlLinkRemove = `
		<a class="btn btn-secondary bi bi-trash float-right"
			href="javascript:removeExtraImage(${index - 1})" 
			title="Remove this image"></a>
	`;

    $("#divProductImages").append(htmlExtraImage);

    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);

    extraImagesCount++;
}

// xóa ExtraImage
function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
}
