package validators;

public class HospedeValidator {

    public static boolean validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        return nome.trim().length() >= 2;
    }

    public static boolean validarDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            return false;
        }
        return documento.trim().length() >= 3;
    }

    //Em teoria todos os nomes tem mais do que dois caracteres
    public static String obterMensagemErroNome() {
        return "Nome inválido. Deve ter pelo menos 2 caracteres.";
    }

    //A confirrmar
    public static String obterMensagemErroDocumento() {
        return "Documento inválido. Deve ter pelo menos 3 caracteres.";
    }

    //Um documento por individuo
    public static String obterMensagemErroDocumentoDuplicado() {
        return "Este documento já está registado para outro hóspede.";
    }
}
