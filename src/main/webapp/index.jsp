<style>
form {
  border: double;
}
</style>
Cadastro<br>
<form action="http://localhost:8080/Facerecognizer/services/upload/register" method="post">
	<input name="name" type="text" /><br><br>
    <button name="submit" type="submit">Upload</button>
</form>
Foto para treinamento<br>
<form action="http://localhost:8080/Facerecognizer/services/upload/training" method="post" enctype="multipart/form-data">
	<input name="file" id="filename" type="file" /><br><br>
	<input name="id" type="number" /><br><br>
    <button name="submit" type="submit">Upload</button>
</form>
Foto para reconhecimento<br>
<form action="http://localhost:8080/Facerecognizer/services/upload/recognize" method="post" enctype="multipart/form-data">
	<input name="file" id="filename" type="file" /><br><br>
    <button name="submit" type="submit">Upload</button>
</form>