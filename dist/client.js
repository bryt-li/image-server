function setImageUrl(text,img){
  if (text.indexOf("@startuml") == 0) {
    $.ajax({
      type: "POST",
      url: "https://utils.orienteexpress.com/uml/text",
      data: text,
      crossDomain: true,
      dataType: "json"
    }).done(function(data) {
      if (data.ok) {
        img.attr("src", "https://utils.orienteexpress.com/uml/png/" + data.payload.hash);
      }
    });
  }else if(text.indexOf('<?xml version="1.0" encoding="UTF-8"?>') == 0){
    $.ajax({
      type: "POST",
      url: "https://utils.orienteexpress.com/wf/text",
      data: text,
      crossDomain: true,
      dataType: "json"
    }).done(function(data) {
      if (data.ok) {
        img.attr("src", "https://utils.orienteexpress.com/wf/png/" + data.payload.hash);
      }
    });
  }
}
