public class StringMatcher {
    private StringBuilder currentString;
    private int matchIndex;

    public StringMatcher() {
        currentString = new StringBuilder();
        matchIndex = 0; // Start matching from the first character of the target string
    }

    public String appendCharacter(char character, String target) {
        // Append the character to the current string
        currentString.append(character);

        if(currentString.length()>=9) System.out.println("Current string: " + currentString);

        // Check if the current character matches the corresponding character in the target
        if (character == target.charAt(matchIndex)) {
            matchIndex++; // Move to the next character in the target

            // Check if the entire target string has been matched
            if (matchIndex == target.length()) {
                String matched = currentString.toString();
                reset(); // Reset for future matches
                return matched; // Return the complete match
            }
        } else {
            // If mismatch occurs, check if there's a potential new match starting from here
            matchIndex = findPartialMatch(character, target);
        }

        return null; // Return null if the target has not been fully matched
    }

    // This method tries to find if a partial match exists when mismatch occurs
    private int findPartialMatch(char character, String target) {
        // Check if the character matches any initial part of the target string
        for (int i = 0; i < target.length(); i++) {
            if (character == target.charAt(i)) {
                currentString.setLength(0); // Reset current string
                currentString.append(character); // Add the character as the new starting point
                return 1; // Start from the next character in the target string
            }
        }
        currentString.setLength(0); // Completely reset if no partial match is found
        return 0;
    }

    private void reset() {
        currentString.setLength(0); // Clear the current string
        matchIndex = 0; // Reset the match index
    }

    public static void main(String[] args) {
        StringMatcher matcher = new StringMatcher();
        String target = "hello";

        // Example of appending characters one by one
        String[] characters = {"y", "e", "h", "h", "e", "l", "l", "o", "!", " "};

        for (String character : characters) {
            String result = matcher.appendCharacter(character.charAt(0), target);
            if (result != null) {
                System.out.println("Target matched: " + result);
                break; // Stop after finding the target
            }
        }
    }
}
