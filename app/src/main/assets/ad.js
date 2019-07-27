function deleteAdNew() {
    /* 知乎顶部栏 */
    findElementDoActionX('div', 'MobileAppHeader-inner', function (tag) {
        tag.remove();
    });
    /* 底部 app 打开按钮 */
    findElementDoActionX('button', 'OpenInAppButton OpenInApp is-shown', function (tag) {
        tag.remove();
    });
    /* 将标题 <a> 替换为 <div>，取消点击效果。 */
    findElementDoActionX('div', 'QuestionHeader-title', function (tag) {
        var div = document.createElement('div');
        div.innerHTML = tag.firstChild.innerHTML;
        tag.replaceChild(div, tag.firstChild);
    });
    /* 点击展开按钮 */
    findElementDoActionX('button', 'Button ContentItem-rightButton Button--plain', function (tag) {
        tag.click();
    });
    /* 删除收起按钮 */
    findElementDoActionX('button', 'Button ContentItem-action ContentItem-rightButton Button--plain', function (tag) {
        tag.remove();
    });
    /* 删除答案之外的元素 */
    var ads = getElementsByClassNameX('div', 'Question-main');
    var child = ads.firstElementChild;
    while (child) {
        if (child.className !== 'Card AnswerCard') {
            child.remove();
        }
        console.log(child);
        child = child.nextElementSibling;
    }
}

function findElementDoActionX(tagName, className, action) {
    var tags = document.getElementsByTagName(tagName);
    for (var i = 0; i < tags.length; i++) {
        if (tags[i].className === className) {
            action(tags[i]);
        }
    }
}

function getElementsByClassNameX(tagName, className) {
    var tags = document.getElementsByTagName(tagName);
    for (var i = 0; i < tags.length; i++) {
        if (tags[i].className === className) {
            return tags[i];
        }
    }
}

deleteAdNew();
deleteAdNew();
deleteAdNew();