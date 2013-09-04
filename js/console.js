
function query_sandbox(url, code) {
  var data = {};
  $.ajax({
    type: "POST",
    url:url,
    data: { expr : code, teacher : ((window.teacher == true) ? true : false) },
    async:false,
    success: function(res) { data = res; },
  });
  return data;
}

function load_str(code) {
  return query_sandbox("execfile.json", code)
}

function eval_clojure(code) {
  return query_sandbox("eval.json", code)
}

function html_escape(val) {
    var result = val;
    result = result.replace(/\n/g, "<br/>");
    result = result.replace(/[<]/g, "&lt;");
    result = result.replace(/[>]/g, "&gt;");
    return result;
}


function onValidate(input) {
    return (input != "");
}

function matchCommand(input){
  switch(input){
    case "(reset)":
      return function(){
        controller.reset();
      };
  }
}

function onHandle(line, report) {
    var input = $.trim(line);

    commandCB = matchCommand(input);

    if (commandCB){
      return commandCB();
    }else{
      // perform evaluation
      var data = eval_clojure(input);

      // handle error
      if (data.error) {
          return [{msg: data.message, className: "jquery-console-message-error"}];
      }

      // display expr results
      return [{msg: data.result, className: "jquery-console-message-value"}];
    }
}

/**
 * This should be called anytime the changer div is updated so it can rebind event listeners.
 * Currently this is just to make the code elements clickable.
 */
function changerUpdated() {
    $("#changer code.expr").each(function() {
        $(this).css("cursor", "pointer");
        $(this).attr("title", "Click to insert '" + $(this).text() + "' into the console.");
        $(this).click(function(e) {
            controller.promptText($(this).text());
            controller.inner.click();
        });
    });
}

function pollTeacherfile() {
  $.ajax({
    type: "GET",
    url: "/teacherfile.json",
    success: function(res) {
       if (res != window.teacherfileEditor.getValue()) {
          window.teacherfileEditor.setValue(res);
      }
     }
  });
  setTimeout(pollTeacherfile, 3000);
}

var controller;

$(document).ready(function() {
    controller = $("#console").console({
        promptLabel: '>>> ',
        commandValidate: onValidate,
        commandHandle: onHandle,
        autofocus:true,
        animateScroll:true,
        promptHistory:true
    });
    changerUpdated();
    pollTeacherfile();
});
