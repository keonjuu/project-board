function save(id){
    const selectedVal = $('#selectJoinYn_' + id.toString()).val();
    location.href="/admin/save/" +id + "/"+ selectedVal ;
    console.log("location.href" + location.href);
}