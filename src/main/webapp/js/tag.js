function getTag(select, i) {
    let current = select.value;
    if (current !== '') {
        let old = document.getElementById('tags:' + i).innerHTML;
        if (!old.includes(current)) {
            let finishTags = old + '#' + current + ' ';
            document.getElementById('tags:' + i).innerHTML = finishTags;
            document.getElementById('tags-input:' + i).value = finishTags;
        }
    }
}

function addTag(i) {

    let newTag = document.getElementById('newTag:' + i).value.replace("</?script>", "").trim().toLowerCase();
    if (isValidTag(newTag, i)) {
        let old = document.getElementById('tags:' + i).innerHTML;
        if (!old.includes(newTag)) {
            let finishTags = old + '#' + newTag + ' ';
            document.getElementById('tags:' + i).innerHTML = finishTags;
            document.getElementById('tags-input:' + i).value = finishTags;
            document.getElementById('newTag:' + i).value = "";
        }
    }
}

function isValidTag(tag, i) {
    if (/^[a-zA-Zа-яА-Я]{3,15}$/.test(tag)) {
        document.getElementById('newTag:' + i).className = "";
        return true;
    } else {
        document.getElementById('newTag:' + i).className = "customSmall";
        return false;
    }
}