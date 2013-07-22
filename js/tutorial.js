$(function(){
  window.currentPage = 0;
  window.lastPage = false;
  
  function styleButtons(){
    $.each($(".navbut"), function(){
      $(this).removeClass("active");
    });
    if(currentPage > 0){
      $("#prev").addClass('active');
    }
    if(!window.lastPage){
      $("#next").addClass('active');
    }
  }

  function loadPage(n){
    $.ajax({
      url: "pages/"+n+".html",
      dataType: "html",
      success:function(data, stat, req){
        window.lastPage = false;
        $("#tutorial").html(data);
        window.currentPage = n;
        prettifyCode();
        styleButtons();
      },
      error:function(req, stat, err){
        console.log(req, stat, err);
        $("#tutorial").html('<p class="error">Failed to load the tutorial.</p>');
      }
    });
  }

  loadPage(window.currentPage);
  
  function nextPage(){
    var np = window.currentPage+1;
    console.log("next page. "+window.currentPage+"->"+np);
    loadPage(window.currentPage + 1);
  }


  function prevPage(){
    var pp = window.currentPage -1; 
    console.log("prev page. "+window.currentPage+"->"+pp); 
    loadPage(window.currentPage - 1);
  }
  
  $("#next").click(function(){
    if($("#next").hasClass('active')){
      nextPage();
    }
  });

  $("#prev").click(function(){
    if($("#prev").hasClass('active')){
      prevPage();
    }
  });

     function heightUpdateFunction(elem, editor) {
        var newHeight =
                  (editor.getSession().getScreenLength()+1)
                  * editor.renderer.lineHeight
                  + editor.renderer.scrollBar.getWidth();
        $(elem).height(newHeight.toString() + "px");
        editor.resize();
    };

    // Set initial size to match initial content
  /* Prettifies the any code class inside the tutorial by wrapping it in an ace editor*/
  function prettifyCode(){
    $.each( $("#tutorial").find(".code"), function(){
      var ae = $('<div>').addClass("codeshow").text($(this).text());
      $(this).empty();
      $(this).append(ae);
      var myeditor = ace.edit(ae.get(0));
     
      myeditor.setTheme("ace/theme/crimson_editor");
      myeditor.setHighlightActiveLine(false);
      myeditor.setReadOnly(true);
      myeditor.renderer.setShowGutter(false);
      myeditor.getSession().setMode("ace/mode/clojure");
      myeditor.getSession().setTabSize(2);
      myeditor.getSession().setUseSoftTabs(true);
      ae.css('fontSize', '20px');
      heightUpdateFunction(ae, myeditor);
      var link = $('<a>').addClass('loadlink').text("add to session").click(function(){
        var session = window.mainEditor.getSession()
        session.insert({row:session.getLength()}, "\n\n"+myeditor.getValue());
        window.mainEditor.savefile(); 
      });
      $(this).append(link);
      $(this).append($('<div>').addClass('clear'));
      
    });
  }

});
