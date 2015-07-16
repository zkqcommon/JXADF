﻿<!DOCTYPE HTML>
<!--
<html lang="zh" manifest="./index.manifest" >
-->
<html>
<head>
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">
<title>导入数据</title>
<meta name="description" content="File Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video for jQuery. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. Works with any server-side platform (PHP, Python, Ruby on Rails, Java, Node.js, Go etc.) that supports standard HTML form file uploads.">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap styles -->
<link rel="stylesheet" href="./css/bootstrap.min.css">
<!-- Generic page styles -->
<link rel="stylesheet" href="css/style.css">
<!-- blueimp Gallery styles -->
<link rel="stylesheet" href="./css/blueimp-gallery.min.css">
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="css/jquery.fileupload-ui.css">
<!-- CSS adjustments for browsers with JavaScript disabled -->
<noscript><link rel="stylesheet" href="css/jquery.fileupload-ui-noscript.css"></noscript>
<link rel="stylesheet" href="css/jxrepair.css">
	<script type='text/javascript'>
            function closeIt() {
                window.open('', '_self');
                window.close();
            }	
		
		
	/**
	 * 重置iframe框架高度，iframe高度自动增长
	 */
	function resetIframeHeightV(){
		var obj = parent.document.getElementById("iframeAttachment");		
        //obj.height = document.body.clientHeight;
        if (obj && !window.opera)
        {
            if (obj.contentDocument && obj.contentDocument.body.offsetHeight) //如果用户的浏览器是NetScape
                obj.height = obj.contentDocument.body.offsetHeight-105;
            else if (obj.Document && obj.document.body.scrollHeight) //如果用户的浏览器是IE
                obj.height = obj.document.body.clientHeight-105;
        }
        
	}	
	$(function(){
	$('#mppfileupload').fileupload('option', {  
    progressall: function (e, data) { 
    	 
        var progress = parseInt(data.loaded / data.total * 100, 10);  
        console.log(progress + '%');  
    }  
});
	})
	</script>
</head>
<body onload="resetIframeHeightV()">
<div class="container">
    <h3>导入数据</h3>
    <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload" action="../mppup/?initVal=<%=request.getParameter("initVal")%>&code=<%=request.getParameter("code")%>&fields=<%=request.getParameter("fields")%>" method="POST" enctype="multipart/form-data">
        <!-- Redirect browsers with JavaScript disabled to the origin page -->
        <noscript><input type="hidden" name="redirect" value="../mppup.action"></noscript>
        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
        <div class="row fileupload-buttonbar">
            <div class="col-lg-7">
                <!-- The fileinput-button span is used to style the file input field as button -->
<%--                 <input id="mppfileupload"  type="file" name="files[]" data-url="../mppup/?fromUid=<%=request.getParameter("fromUid")%>&code=<%=request.getParameter("code")%>" multiple>
 --%>               
                <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>选择文件</span>
                    <input type="file" name="files[]"  accept="image/mpp" multiple>
                </span>
                <button type="submit" class="btn btn-primary start">
                    <i class=""></i>
                    <span>导入</span>
                </button>
           <!--      <button type="reset" class="btn btn-warning cancel">
                    <i class=""></i>
                    <span>取消</span>
                </button> -->
         <!--        <button type="button" class="btn btn-danger back" onclick="closeIt()">
                    <i class=""></i>
                    <span>关闭</span>
                </button> -->
                <!-- <input type="checkbox" class="toggle"> -->
                <!-- The loading indicator is shown during file processing -->
                <span class="fileupload-loading"></span>
            </div>
            <!-- The global progress information -->
            <div class="col-lg-5 fileupload-progress fade">
                <!-- The global progress bar -->
                <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar progress-bar-success" style="width:0%;"></div>
                </div>
                <!-- The extended global progress information -->
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        <!-- The table listing the files available for upload/download -->
        <table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>
    </form>
    <br>
</div>
<!-- The blueimp Gallery widget -->
<div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">‹</a>
    <a class="next">›</a>
    <a class="close">×</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>
<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            {% if (file.error) { %}
                <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <p class="size">{%=o.formatFileSize(file.size)%}</p>
            {% if (!o.files.error) { %}
                <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
            {% } %}
        </td>
        <td>
            {% if (!o.files.error && !i && !o.options.autoUpload) { %}
                <button class="btn btn-primary start">
                    <i class=""></i>
                    <span>导入</span>
                </button>
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-warning cancel">
                    <i class=""></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.thumbnailUrl) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                {% if (file.url) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                    <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
                <div><span class="label label-danger">Error</span> {%=file.error%}</div>
 			{% } else { %}
				 <div><span class="label label-danger">{%=file.success%}</div>
            {% } %}
                   
        </td>

        <td>
            {% if (file.deleteUrl) { %}
                <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class=""></i>
                    <span>删除</span>
                </button>
                <input type="checkbox" name="delete" value="1" class="toggle">
            {% } else if (file.error) { %}
                 <button class="btn btn-warning cancel">
                    <i class=""></i>
                    <span>取消</span>
                </button> 
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<script>
<%-- <%-- var evaUpload = function(){

    var uploader = $("#fileupload");
    uploader.fileupload({
        url : '../mppup/?fromUid=<%=request.getParameter("fromUid")%>&code=<%=request.getParameter("code")%>',
        dataType: 'json',
        autoUpload: true,
        acceptFileTypes:  /(\.|\/)(gif|jpe?g|png)$/i,
        maxNumberOfFiles : 1,
        fileInput : uploader.find("input:file"),
        maxFileSize: 5000000,
        previewMaxWidth : 200,
        previewMaxHeight : 200    
    });


    uploader.bind('fileuploadfailed', function (e, data) {
            //eva.p(data);
    });
    uploader.bind('fileuploadadded', function (e, data) {
            if(!data.files.valid) {
                    //uploader.find('.files .cancel').click();
            }
    });
    uploader.bind('fileuploadchange', function (e, data) {
            //uploader.find('.files').empty();
    });

    //Fix firefox input disable attr issue
    uploader.find("input:file").removeAttr('disabled');
}
eva.ready(function(){
	evaUpload();
}); --%> 


</script>
<script src="../javascript/jquery-ui-1.10.3/jquery-1.9.1.js"></script>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="js/vendor/jquery.ui.widget.js"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="js/tmpl.min.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="js/load-image.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="js/canvas-to-blob.min.js"></script>
<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
<script src="js/bootstrap.min.js"></script>
<!-- blueimp Gallery script -->
<script src="js/jquery.blueimp-gallery.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="js/jquery.fileupload.js"></script>
<!-- The File Upload processing plugin -->
<script src="js/jquery.fileupload-process.js"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="js/jquery.fileupload-image.js"></script>
<!-- The File Upload audio preview plugin -->
<script src="js/jquery.fileupload-audio.js"></script>
<!-- The File Upload video preview plugin -->
<script src="js/jquery.fileupload-video.js"></script>
<!-- The File Upload validation plugin -->
<script src="js/jquery.fileupload-validate.js"></script>
<!-- The File Upload user interface plugin -->
<script src="js/jquery.fileupload-ui.js"></script>
<!-- The main application script -->
<script src="js/main.js"></script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="js/cors/jquery.xdr-transport.js"></script>
<![endif]-->
</body> 
</html>
