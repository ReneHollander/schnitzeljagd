$(function () {
    var orderField = $("#order");
    var stations = $("#stations").find("tbody");

    function applyOrder() {
        var order = [];
        stations.children().each(function (key, value) {
            order.push($(value).attr('stationid'));
        });
        orderField.attr('value', order);
    }

    applyOrder();

    stations.sortable({
        helper: function (e, tr) {
            console.log(e);
            var $originals = tr.children();
            var $helper = tr.clone();
            $helper.children().each(function (index) {
                $(this).width($originals.eq(index).width())
            });
            return $helper;
        },
        stop: applyOrder
    }).disableSelection();

    $('input').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue',
        increaseArea: '20%'
    });

    function tabnameSetter(targettab, targetinput) {
        targettab.on('shown.bs.tab', function (e) {
            var target = $(e.target);
            targetinput.attr('value', target.attr('tabname'));
        })
    }

    tabnameSetter($('#navigationTabs'), $('#navigationtype'));
    tabnameSetter($('#answerTabs'), $('#answertype'));

    var navigationCompassMap = new google.maps.Map(document.getElementById('navigationCompassMap'), {
        center: {lat: 48.305277, lng: 16.326468},
        zoom: 16
    });
    var navigationCompassMapMarker = new google.maps.Marker({map: navigationCompassMap});
    navigationCompassMap.addListener('click', function (e) {
        navigationCompassMapMarker.setPosition(e.latLng);
        $('#compass_lat').val(e.latLng.lat());
        $('#compass_long').val(e.latLng.lng());
    });

    var navigationMapMap;
    $('#navigationTabs').on('shown.bs.tab', function (e) {
        if ($(e.target).attr('tabname') === 'map' && !navigationMapMap) {
            navigationMapMap = new google.maps.Map(document.getElementById('navigationMapMap'), {
                center: {lat: 48.305277, lng: 16.326468},
                zoom: 16
            });
        }
    });


    var answerAreaMap;
    var areaPolygon = new google.maps.Polygon({
        paths: [],
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 3,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        draggable: true,
        editable: true
    });
    $('#answerTabs').on('shown.bs.tab', function (e) {
        if ($(e.target).attr('tabname') === 'area' && !answerAreaMap) {
            answerAreaMap = new google.maps.Map(document.getElementById('answerAreaMap'), {
                center: {lat: 48.305277, lng: 16.326468},
                zoom: 16
            });
            areaPolygon.setMap(answerAreaMap);
            answerAreaMap.addListener('click', function (e) {
                var path = areaPolygon.getPath();
                if (!path) path = [];
                path.push(e.latLng);
                areaPolygon.setPath(path);
            });
        }
    })

});
