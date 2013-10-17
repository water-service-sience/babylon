var map = null;
function initialize(){
    var mapOptions = {
        zoom: 17,
        center: new google.maps.LatLng(35.715492,139.759569),
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);

    google.maps.event.addListener(map,"center_changed",function(){

        var lat = map.center.lat();
        var lon = map.center.lng();
        getPostsAt(lat,lon);

    });

    navigator.geolocation.watchPosition(updateLocation);



}

google.maps.event.addDomListener(window, 'load', initialize);


var markers = [];

function addMarker(post){

    var myLatlng = new google.maps.LatLng(post.latitude,post.longitude);
    var marker = new google.maps.Marker({
        position: myLatlng,
        map: map,
        title: 'Hello World!'
    });
    markers.push(marker);
    google.maps.event.addListener(marker, 'click', function() {
        $("#thumb").attr("src","../images/" + post.imageFile)

        var title = post.comment;
        if(title.length > 15){
            title = title.substring(0,13) + "...";
        }else if(title.length == 0){
            title = ".";
        }
        $("#image-title").text(title);

        $("#balloon").fadeIn();
    });

}

function clearMarkers(){

    for(i in markers){
        markers[i].setMap(null);
    }
    markers = [];
}

function updateLocation(e){

    console.log("lat = " + e.coords.latitude);
    map.panTo(new google.maps.LatLng(
      e.coords.latitude,
      e.coords.longitude));



}


function getPostsAt(lat , lon) {

    var bounds = map.getBounds();
    var dLat = Math.abs(bounds.getNorthEast().lat() - bounds.getSouthWest().lat());
    var dLon = Math.abs(bounds.getNorthEast().lng() - bounds.getSouthWest().lng());


    if( Math.abs(lat - lastLat) > dLat / 2 ||
        Math.abs(lon - lastLon) > dLon / 2){
        forceGetPostsAt(lat,lon,Math.max(dLat,dLon));
    }


}

var lastLat = 0;
var lastLon = 0;
var category = 1;


function forceGetPostsAt(lat,lon,range){

    console.log("Begin to get");
    lastLat = lat;
    lastLon = lon;


    $.getJSON("../event/post/near?categoryIds=" + category +
      "&latitude=" + lat +
      "&longitude=" + lon +
      "&range=" + range,
    function(data) {
        //console.log(JSON.stringify(data));

        clearMarkers();

        for(i in data){
            var post = data[i];
            addMarker(post);
        }
    });
}

function onCloseWindow(){

    $("#balloon").hide();
}

function getUrlVars()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}


$(function(){
    console.log("Get gps");
    $("#balloon").hide();

    var v = getUrlVars();
    category = v["category"] | 1;

});
