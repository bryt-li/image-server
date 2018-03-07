function setImageUrl(text,img){
  if (text.indexOf("@startuml") == 0) {
    $.ajax({
      type: "POST",
      url: "https://utils.orienteexpress.com/image_server/text",
      data: text,
      crossDomain: true,
      dataType: "json"
    }).done(function(data) {
      if (data.ok) {
        img.attr("src", data.payload.url);
      }
    });
  }else if(text.indexOf('<?xml version="1.0" encoding="UTF-8"?>') == 0){
    $.ajax({
      type: "POST",
      url: "https://utils.orienteexpress.com/image_server/text",
      data: text,
      crossDomain: true,
      dataType: "json"
    }).done(function(data) {
      if (data.ok) {
        img.attr("src", data.payload.url);
      }
    });
  }
}
