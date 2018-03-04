# README #

Deploy BPMN model from http URL and render image.

## Build
	cd activiti_image_server
	mvn package
	docker build -t activiti_image_server .

## Run
	docker run -p 9901:8080 activiti_image_server

## Usage
	
### Post embeded activiti xml

Please modify dist/plantuml.js to meet your own requirement.

```
<script src="/dist/plantuml.js"></script>

<script type="text/javascript">
$(document).ready(function() {
  enablePlantUML('{{ site.plantuml_host }}');
});
</script>

```

### Extenal URL bpmn file

```
![Activiti Example](http://servername/url?src=https://bryt-li.github.io/bpmn/FooProcess.bpmn)
```
