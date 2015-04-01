$ ->
  $.get "/tasks", (data) ->
    $.each data, (index, task) ->
      $("#tasks").append $("<li>").text task.contents