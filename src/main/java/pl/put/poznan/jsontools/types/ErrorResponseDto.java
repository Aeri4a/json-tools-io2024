package pl.put.poznan.jsontools.types;

public class ErrorResponseDto {
    private String error;


    /**
     * Konstruktor klasy ErrorResponseDto.
     *
     * @param error zwraca wiadomość dla zapytania powodującego błąd serwera
     */
    public ErrorResponseDto(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}