
public class CaretCoordinates {

    private int line;
    private int column;

    public CaretCoordinates(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }
}
