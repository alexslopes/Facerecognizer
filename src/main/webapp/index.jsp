<style>
form {
  border: double;
}
</style>
Cadastro<br>
<form action="http://localhost:8080/Facerecognizer/services/upload/register" method="post">
	Login<br>
	<input name="login" type="text" /><br><br>
	Senha<br>
	<input name="password" type="text" /><br><br>
	Nome<br>
	<input name="name" type="text" /><br><br>
    <button name="submit" type="submit">Cadastrar</button>
</form>
Login<br>
<form action="http://localhost:8080/Facerecognizer/services/upload/login" method="post">
	Login<br>
	<input name="login" type="text" /><br><br>
	Senha<br>
	<input name="password" type="text" /><br><br>
	<button name="submit" type="submit">Logar</button>
</form>
Foto para treinamento<br>
<form action="http://localhost:8080/Facerecognizer/services/upload/training" method="post" enctype="multipart/form-data">
	<input name="file" id="filename" type="file" /><br><br>
	<input name="login" type="text" /><br><br>
	<input name="password" type="text" /><br><br>
    <button name="submit" type="submit">Upload</button>
</form>
Foto para reconhecimento<br>
<form action="http://localhost:8080/Facerecognizer/services/upload/recognize" method="post" enctype="multipart/form-data">
	<input name="file" id="filename" type="file" /><br><br>
    <button name="submit" type="submit">Upload</button>
</form>