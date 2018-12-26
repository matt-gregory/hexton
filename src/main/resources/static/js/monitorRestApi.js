var MonitorRestApi = function () {

    function getObject(url, success, error) {
        $.ajax({
            type: "GET",
            url: url,
            dataType: 'json',
            success: function (data) {
                if ($.isArray(data)) {
                    data = data[0];
                }
                success(data);
            },
            error: error
        });
    }

    function getString(url, success, error) {
        $.ajax({
            type: "GET",
            url: url,
            success: function (data) {
                success(data);
            },
            error: error
        });
    }

    function postObject(url, object, success, error) {
        $.ajax({
            type: "POST",
            url: url,
            processData: false,
            data: JSON.stringify(object),
            contentType: 'application/json',
            success: success,
            error: error
        });
    }

    return {
        getMonitorStats: function (success, error) {
            getObject('/monitorRates', success, error);
        },
        getLastSmsMessage: function (success, error) {
            getObject('/lastSmsMessage', success, error);
        },
        getLastUssdMessage: function (success, error) {
            getObject('/lastUssdMessage', success, error);
        },
        getLatency: function (success, error) {
            getObject('/getLatency', success, error);
        },
        setLatency: function (latency, success, error) {
            postObject('/setLatency', latency, success, error);
        },
        getUssdAutoResponse: function (success, error) {
            getObject('/ussdAutoResponse', success, error);
        },
        setUssdAutoResponse: function (ussdAutoResponse, success, error) {
            postObject('/ussdAutoResponse', ussdAutoResponse, success, error);
        },
        getUssdDelay: function (success, error) {
            getObject('/ussdDelay', success, error);
        },
        setUssdDelay: function (ussdDelay, success, error) {
            postObject('/ussdDelay', ussdDelay, success, error);
        },
        getFailureChance: function (success, error) {
            getObject('/getFailureChance', success, error);
        },
        setFailureChance: function (failureChance, success, error) {
            postObject('/setFailureChance', failureChance, success, error);
        },
        emulatorResponse: function(message, success, error){
            postObject('/emulator/message', message, success, error);
        },
        emulatorTimeout: function(message, success, error){
            postObject('/emulator/timeout', message, success, error);
        },
        emulatorPing: function(number, success, error){
            getString('/emulator/ping/' + number, success, error);
        }
    }
}