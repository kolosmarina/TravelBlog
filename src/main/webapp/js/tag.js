
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
    if (newTag !== '') {
        let old = document.getElementById('tags:' + i).innerHTML;
        if (!old.includes(newTag)) {
            let finishTags = old + '#' + newTag + ' ';
            document.getElementById('tags:' + i).innerHTML = finishTags;
            document.getElementById('tags-input:' + i).value = finishTags;
            document.getElementById('newTag:' + i).value = "";
        }
    }
}
