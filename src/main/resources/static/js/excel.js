
/**
 * blob文件下载
 * 如果blob的type为"application/json"，则解析失败，返回异常信息
 *
 * @param fileBytes blob文件流
 * @param fileName 文件名称
 */
function download(response) {
    // 如果返回的blob数据类型是JSON，则进行格式化输出
    if (response.data.type.indexOf("application/json") === 0) {
        const reader = new FileReader()
        reader.addEventListener("loadend", () => {
            // 将会打印json格式
            let obj = reader.result;
            if (obj instanceof ArrayBuffer) {
                // ArrayBuffer => string
                const o = String.fromCharCode.apply(null, new Uint8Array(obj))
                alert(JSON.parse(o).message)
            } else {
                alert(JSON.parse(obj).message)
            }
        })
        // 如果转换完中文出现乱码，可以设置一下代码
        reader.readAsText(response.data, "utf-8")
    } else {
        downloadExcelFile(response.data, decodeURIComponent(response.headers["excel-name"]))
    }
}

/**
 * blob文件下载
 * @param fileBytes blob文件流
 * @param fileName 文件名称
 */
function downloadExcelFile(fileBytes, fileName) {
    // const blobFileType = 'application/vnd.ms-excel'
    const blobFileType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    downloadFile(fileBytes, fileName, blobFileType)
}

/**
 * blob文件下载
 * @param fileBytes blob文件流
 * @param fileName 文件名称
 * @param blobFileType 下载文件的blob类型（字符串）
 */
function downloadFile(fileBytes, fileName, blobFileType) {
    if (!fileBytes || !fileName || !blobFileType) {
        alert("缺失必要的参数: params{fileBytes, fileName, blobFileType}")
        return
    }
    const blob = new Blob([fileBytes], {type: blobFileType});
    if (window.navigator.msSaveOrOpenBlob) { // 兼容IE10
        navigator.msSaveBlob(blob, fileName)
    } else {
        const a = document.createElement("a");
        const url = window.URL.createObjectURL(blob);
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
    }
}