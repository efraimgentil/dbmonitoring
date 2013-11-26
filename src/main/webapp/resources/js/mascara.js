/*Função  Pai de Mascaras*/
function Mascara(o, f) {
	v_obj = o;
	v_fun = f;
	setTimeout("execmascara()", 1);
}

/* Função que Executa os objetos */
function execmascara() {
	v_obj.value = v_fun(v_obj.value);
}

/* Função que Determina as expressões regulares dos objetos */
function leech(v) {
	v = v.replace(/o/gi, "0");
	v = v.replace(/i/gi, "1");
	v = v.replace(/z/gi, "2");
	v = v.replace(/e/gi, "3");
	v = v.replace(/a/gi, "4");
	v = v.replace(/s/gi, "5");
	v = v.replace(/t/gi, "7");
	return v;
}

/* Função que permite apenas numeros */
function Integer(v) {
	return v.replace(/\D/g, "");
}

/* Função que padroniza telefone (11) 4184-1241 */
function Telefone(v) {
	v = v || "";
	if(v.length > "(00) 0000-00000".length){
		v = v.substr(0, "(11) 4184-12410".length );
	}else{
		v = v.replace(/\D/g, "");
		v = v.replace(/^(\d\d)(\d)/g, "($1) $2");
		v = v.replace(/(\d{4})(\d)/, "$1-$2");
	}
	return v;
}

/* Função que padroniza telefone (11) 41841241 */
function TelefoneCall(v) {
		v = v.replace(/\D/g, "");
		v = v.replace(/^(\d\d)(\d)/g, "($1) $2");
	return v;
}

/* Função que padroniza CPF */
function Cpf(v) {
	v = v || "";
	if(v.length > "000.000.000-00".length){
		v = v.substr(0,14);
	}else{
		v = v.replace(/\D/g, "");
		v = v.replace(/(\d{3})(\d)/, "$1.$2");
		v = v.replace(/(\d{3})(\d)/, "$1.$2");
		v = v.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
	}
	return v;
}

/**
 * Recebe um valor e formata como um cep e o retorna
 * @param v
 * @returns
 */
function Cep(v) {
	var mask = "00000-000";
	if(v.length > mask.length){
		v = v.substr( 0 , mask.length );
	}else{
		v = v.replace(/D/g, "");
		v = v.replace(/^(\d{5})(\d)/, "$1-$2");
	}
	return v;
}

/**
 * Recebe um valor e formata como cnpj e o retorna
 * @param v
 * @returns
 */
function Cnpj(v) {
	var mask = "00.000.000/0000-00";
	if(v.length > mask.length){
		v = v.substr(0, mask.length );
	}else{
		v = v.replace(/\D/g, "");
		v = v.replace(/^(\d{2})(\d)/, "$1.$2");
		v = v.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3");
		v = v.replace(/\.(\d{3})(\d)/, ".$1/$2");
		v = v.replace(/(\d{4})(\d)/, "$1-$2");
	}
	return v;
}

/* Função que permite apenas numeros Romanos */
function Romanos(v) {
	v = v.toUpperCase();
	v = v.replace(/[^IVXLCDM]/g, "");

	while (v.replace(
			/^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/, "") != "")
		v = v.replace(/.$/, "");
	return v;
}

/* Função que padroniza o Site */
function Site(v) {
	v = v.replace(/^http:\/\/?/, "");
	dominio = v;
	caminho = "";
	if (v.indexOf("/") > -1)
		dominio = v.split("/")[0];
	caminho = v.replace(/[^\/]*/, "");
	dominio = dominio.replace(/[^\w\.\+-:@]/g, "");
	caminho = caminho.replace(/[^\w\d\+-@:\?&=%\(\)\.]/g, "");
	caminho = caminho.replace(/([\?&])=/, "$1");
	if (caminho != "")
		dominio = dominio.replace(/\.+$/, "");
	v = "http://" + dominio + caminho;
	return v;
}

/**
 * Recebe um valor formata como data (dd/MM/yyyy) e o retorna
 * @param v
 * @returns
 */
function Data(v) {
	var mask = "00/00/0000";
	if(v.length > mask.length){
		v = v.substr(0, mask.length );
	}else{
		v = v.replace(/\D/g, "");
		v = v.replace(/(\d{2})(\d)/, "$1/$2");
		v = v.replace(/(\d{2})(\d)/, "$1/$2");
	}
	return v;
}

/**
 * Recebe um valor e formata como hora (HH:mm) e o retorna
 * @param v
 * @returns
 */
function Hora(v) {
	v = v.replace(/\D/g, "");
	v = v.replace(/(\d{2})(\d)/, "$1:$2");
	return v;
}

/* Função que padroniza valor monétario */
function Valor(v) {
	v = v.replace(/\D/g, ""); // Remove tudo o que não é dígito
	v = v.replace(/^([0-9]{3}\.?){3}-[0-9]{2}$/, "$1.$2");
	// v=v.replace(/(\d{3})(\d)/g,"$1,$2")
	v = v.replace(/(\d)(\d{2})$/, "$1.$2"); // Coloca ponto antes dos 2 últimos
											// digitos
	return v;
}

/* Função que padroniza Area */
function Area(v) {
	v = v.replace(/\D/g, "");
	v = v.replace(/(\d)(\d{2})$/, "$1.$2");
	return v;
}

function formataNumero(nStr , length)
{
	length = (length == undefined) ? 2 : length;
	nStr = parseFloat(nStr);
	nStr = nStr.toFixed(length);
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? ',' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + '.' + '$2');
	}
	return x1 + x2;
}



function validaCNPJ(cnpj) {

	var i = 0;
	var l = 0;
	var strNum = "";
	var strMul = "6543298765432";
	var character = "";
	var iValido = 1;
	var iSoma = 0;
	var strNum_base = "";
	var iLenNum_base = 0;
	var iLenMul = 0;
	var iSoma = 0;
	var strNum_base = 0;
	var iLenNum_base = 0;

	if (cnpj == "")
		return false; // ("Preencha o campo CNPJ.");

	l = cnpj.length;
	for (i = 0; i < l; i++) {
		caracter = cnpj.substring(i, i + 1)
		if ((caracter >= '0') && (caracter <= '9'))
			strNum = strNum + caracter;
	}
	;

	if (strNum.length != 14)
		return false; // alert("CNPJ deve conter 14 caracteres.");

	strNum_base = strNum.substring(0, 12);
	iLenNum_base = strNum_base.length - 1;
	iLenMul = strMul.length - 1;
	for (i = 0; i < 12; i++)
		iSoma = iSoma
				+ parseInt(strNum_base.substring((iLenNum_base - i),
						(iLenNum_base - i) + 1), 10)
				* parseInt(strMul.substring((iLenMul - i), (iLenMul - i) + 1),
						10);

	iSoma = 11 - (iSoma - Math.floor(iSoma / 11) * 11);
	if (iSoma == 11 || iSoma == 10)
		iSoma = 0;

	strNum_base = strNum_base + iSoma;
	iSoma = 0;
	iLenNum_base = strNum_base.length - 1
	for (i = 0; i < 13; i++)
		iSoma = iSoma
				+ parseInt(strNum_base.substring((iLenNum_base - i),
						(iLenNum_base - i) + 1), 10)
				* parseInt(strMul.substring((iLenMul - i), (iLenMul - i) + 1),
						10)

	iSoma = 11 - (iSoma - Math.floor(iSoma / 11) * 11);
	if (iSoma == 11 || iSoma == 10)
		iSoma = 0;
	strNum_base = strNum_base + iSoma;
	if (strNum != strNum_base)
		return false; // alert ("CNPJ inválido.");

	return true; // alert ("OK");

}

function valida_cpf(cpf) {
	var numeros, digitos, soma, i, resultado, digitos_iguais;
	digitos_iguais = 1;

	cpf = cpf.replace(/\D/g, ""); // remove separadores

	if (cpf.length < 11)
		return false;
	for (i = 0; i < cpf.length - 1; i++)
		if (cpf.charAt(i) != cpf.charAt(i + 1)) {
			digitos_iguais = 0;
			break;
		}
	if (!digitos_iguais) {
		numeros = cpf.substring(0, 9);
		digitos = cpf.substring(9);
		soma = 0;
		for (i = 10; i > 1; i--)
			soma += numeros.charAt(10 - i) * i;
		resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
		if (resultado != digitos.charAt(0))
			return false;
		numeros = cpf.substring(0, 10);
		soma = 0;
		for (i = 11; i > 1; i--)
			soma += numeros.charAt(11 - i) * i;
		resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
		if (resultado != digitos.charAt(1))
			return false;
		return true;
	} else
		return false;
}

function validarEmail(value) {
	var exclude = /[^@\-\.\w]|^[_@\.\-]|[\._\-]{2}|[@\.]{2}|(@)[^@]*\1/;
	var check = /@[\w\-]+\./;
	var checkend = /\.[a-zA-Z]{2,3}$/;

	if (((value.search(exclude) != -1) || (value.search(check)) == -1)
			|| (value.search(checkend) == -1)) {
		return false;
	} else {
		return true;
	}
}