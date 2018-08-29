(function() {
    'use strict';

    var canvas = document.getElementById('canvas');

    var engine = new Shape.Engine(canvas);
    var promise = new Promise((resolve) => { resolve(); });
    
    document.getElementById('go').addEventListener('click', start);
    
    function start() {
        document.getElementById('go').removeEventListener('click', start);
        promise.then(() => engine.toText('I'))
        .then(() => engine.shake())
        .then(() => engine.toText('LOVE'))
        .then(() => engine.shake())
        .then(() => engine.toText('YOU'))
        .then(() => engine.shake())
        .then(() => engine.toText('DAISY'))
        .then(() => engine.shake())
        .then(() => engine.clear())
        .then(() => document.getElementById('go').addEventListener('click', start));
    }
})();


window.onload=function (){
    document.getElementById('go').click();
}