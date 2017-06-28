public class TestExceptions {

    private static void DoTest() {
        System.out.println("Starting static test...");
        
        try {
            throw new ArrayIndexOutOfBoundsException();
        } catch (Exception e) {
            System.out.println("AOOFEx caught");
        }
        
        try {
            try {
                throw new IllegalArgumentException();
            } finally {
                System.out.println("IAEx pass-trough");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("IAEx caught");
        }

        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            System.out.println("REx caught");
        }
    }

    private void DoTest2() {
        System.out.println("Starting instance test...");
        
        try {
            throw new ArrayIndexOutOfBoundsException();
        } catch (Exception e) {
            System.out.println("AOOFEx caught");
        }
        
        try {
            try {
                throw new IllegalArgumentException();
            } finally {
                System.out.println("IAEx pass-trough");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("IAEx caught");
        }

        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            System.out.println("REx caught");
            throw new IllegalArgumentException();
        }
    }
        
	public static void main(String[] args) throws InterruptedException {
        DoTest();
        TestExceptions test = new TestExceptions();
        test.DoTest2();
    }
}