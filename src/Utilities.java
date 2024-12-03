public class Utilities {
    public Utilities() {}

    // Given a 3x3 array, check if a player wins and return its number
    public static int checkWinner(int[][] arr) {
        assert arr.length == 3;
        for (int i = 0; i < 3; i++) {
            if (arr[0][i] != 0 && arr[0][i] == arr[1][i] && arr[0][i] == arr[2][i]) {
                return arr[0][i];
            } else if (arr[i][0] != 0 && arr[i][0] == arr[i][1] && arr[i][0] == arr[i][2]) {
                return arr[i][0];
            }
        }
        if (arr[0][2] != 0 && arr[0][2] == arr[1][1] && arr[0][2] == arr[2][0]) {
            return arr[0][2];
        } else if (arr[0][0] != 0 && arr[0][0] == arr[1][1] && arr[0][0] == arr[2][2]) {
            return arr[0][0];
        }
        return 0;
    }

}
