function init() {
  registrarMensagem("Iniciando socket...")
  registerHandler();
};
function registerHandler() {
  var eventBus = new EventBus('http://localhost:8082/localizacoes');
  eventBus.onopen = function () {
    eventBus.registerHandler('novaLocalizacao', function (error, message) {
      registrarMensagem(message.body);
    });
  }
};
function registrarMensagem(mensagem) {
  var localizacao = $("textarea.localizacao").val();
  localizacao = (mensagem + '\n\n' + localizacao);
  $("textarea.localizacao").val(localizacao);
};
init();