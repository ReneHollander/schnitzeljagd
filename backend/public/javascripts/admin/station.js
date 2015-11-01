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
});