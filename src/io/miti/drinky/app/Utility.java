package io.miti.drinky.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class has various utility methods used by the other classes.
 * 
 * @author Mike Wallace
 * @version 1.0
 */
public final class Utility
{
  /**
   * The list of special XML characters that need to be replaced
   * by XML entities.  This list and saXmlEntities must have the
   * same number of items.
   */
  private static char[] caXmlChars =
    new char[] {'<', '>', '&', '"', '\''};
  
  /**
   * The list of XML entities that replace special XML characters.
   * This list and caXmlChars must have the same number of items.
   */
  private static String[] saXmlEntities =
    new String[] {"&lt;", "&gt;", "&amp;", "&quot;", "&apos;"};
  
  
  /**
   * Default constructor.  Make it private so the class cannot
   * be instantiated.
   */
  private Utility()
  {
    super();
  }
  
  
  /**
   * Converts the special characters in a string into XML entities.
   * 
   * @param inputString the input string to parse
   * @return the input string with special XML characters replaced by entities
   */
  public static String convertToXml(final String inputString)
  {
    // Check for a null or empty string
    if ((inputString == null) || (inputString.length() < 1))
    {
      return "";
    }
    
    // Save the number of items in the two arrays
    final int nNumXmlChars = caXmlChars.length;
    final int nNumXmlEntities = saXmlEntities.length;
    
    // Verify the two lists have the same size
    if (nNumXmlChars != nNumXmlEntities)
    {
      throw new RuntimeException("The two XML arrays have different sizes.");
    }
    
    // See if they have any items
    if (nNumXmlChars == 0)
    {
      // Nothing to do
      return inputString;
    }
    
    // Iterate through the items
    boolean bNeedReplacing = false;
    for (int i = 0; (i < nNumXmlChars) && (!bNeedReplacing); ++i)
    {
      // Check for the existence of the current character
      bNeedReplacing = (inputString.indexOf(caXmlChars[i]) >= 0);
    }
    
    // See if we need to replace any characters
    if (!bNeedReplacing)
    {
      // None of the special characters were found
      return inputString;
    }
    
    // At least one of the special characters was found, so we
    // need to look at each character in the input string
    final int nLength = inputString.length();
    
    // Allocate our temporary buffer
    StringBuffer buf = new StringBuffer(Math.max(100, (nLength + 20)));
    
    // Loop through the string
    char ch;
    for (int i = 0; i < nLength; ++i)
    {
      // Save the current character
      ch = inputString.charAt(i);
      
      // Check for each special character
      boolean bMatchFound = false;
      for (int j = 0; (j < nNumXmlChars) && (!bMatchFound); ++j)
      {
        // Compare the current character against each special character
        if (ch == caXmlChars[j])
        {
          // We found a match, so append the entity corresponding
          // to this special character
          buf.append(saXmlEntities[j]);
          bMatchFound = true;
        }
      }
      
      // If no match was found, append the original character
      if (!bMatchFound)
      {
        buf.append(ch);
      }
    }
    
    // Return the buffer
    return buf.toString();
  }
  
  
  /**
   * Convert the specified string into an integer.
   * 
   * @param str the string to convert
   * @return the integer value for the string
   */
  private static int convertStringToInt(final String str)
  {
    int val = -1;
    
    try
    {
      val = Integer.parseInt(str);
    }
    catch (NumberFormatException nfe)
    {
      val = -1;
    }
    
    return val;
  }
  
  
  /**
   * Get the specified date as a String.
   * 
   * @param year the year (four digits)
   * @param month the month (two digits)
   * @param day the day (two digits)
   * @return the generated string
   */
  public static String getDateAsString(final String year,
                                       final String month,
                                       final String day)
  {
    // Get the numeric values
    final int nYear = convertStringToInt(year);
    final int nMonth = convertStringToInt(month);
    final int nDay = convertStringToInt(day);
    
    // Check that the input values are right
    if ((nYear < 0) || (nMonth < 0) || (nDay < 0))
    {
      return null;
    }
    
    // Construct a calendar
    Calendar cal = new GregorianCalendar(nYear, nMonth - 1, nDay);
    
    // Create our formatter
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    
    // Apply the format to the date and return it
    return formatter.format(cal.getTime());
  }
  
  
  /**
   * Get the specified date as a String.
   * 
   * @param lTime the time to generate a String for
   * @return the generated string
   */
  public static String getDateAsString(final long lTime)
  {
    // Construct a calendar
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(lTime);
    
    // Create our formatter
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    
    // Apply the format to the date and return it
    return formatter.format(cal.getTime());
  }
  
  
  /**
   * Update non-Western characters with the closest version.
   * 
   * @param line the string to check and modify
   * @return the (possibly modified) string
   */
  public static String updateBytes(final String line)
  {
    // Check the input parameter
    if (line == null)
    {
      return line;
    }
    
    // Get the line as a series of bytes
    byte[] data = line.getBytes();
    
    // Record whether any international characters were found
    boolean bInternationalCharsFound = false;

    // Iterate through the bytes to update the binary characters
    final int nSize = data.length;
    for (int nIndex = 0; nIndex < nSize; ++nIndex)
    {
      // Convert the byte to an integer
      final int i = (int) data[nIndex];

      // If it's not binary, skip to the next byte
      if ((i >= 0) && (i <= 127))
      {
        continue;
      }
      
      // We hit one
      bInternationalCharsFound = true;

      // Check the character
      switch (i)
      {
        case -28:    // a w/umlaut
        case -32:    // a w/`
        case -31:    // a w/' (forward accent)
          data[nIndex] = 'a';
          break;

        case -64:    // A w/accent
          data[nIndex] = 'A';
          break;

        case -23:    // e w/'
        case -22:    // e w/^
        case -21:    // e w/umlaut
        case -24:    // e w/`
          data[nIndex] = 'e';
          break;

        case -55:    // E w/'
          data[nIndex] = 'E';
          break;

        case -17:    // i w/umlaut
        case -20:    // i w/`
        case -19:    // i w/'
          data[nIndex] = 'i';
          break;

        case -50:    // I w/^
          data[nIndex] = 'I';
          break;

        case -79:    // +/-
        case -15:    // n w/tilda
          data[nIndex] = 'n';
          break;

        case -10:    // o w/umlaut
        case -13:    // o w/'
          data[nIndex] = 'o';
          break;

        case -42:    // O w/umlaut
          data[nIndex] = 'O';
          break;

        case -4:    // u w/umlaut
          data[nIndex] = 'u';
          break;
        
        case -76:    // '
          data[nIndex] = '\'';
          break;
        
        // Currently not handling -123 (ellipses)
        default:     // it's some other binary character
          break;
      }
    }
    
    // Check for international characters
    if (!bInternationalCharsFound)
    {
      // None found, so return the original string
      return line;
    }
    
    // Return a string from the set of bytes
    return (new String(data));
  }
  
  
  /**
   * Build the string for this object.
   *
   * @param label the tag label
   * @param value the tag value
   * @return the generated HTML string
   */
  private static String buildHtmlSnippet(final String label,
                                         final String value)
  {
    // Declare our string builder
    StringBuilder sb = new StringBuilder(200);
    
    // Append the label, in bold
    sb.append("<b>").append(label).append(":</b> ");
    
    // Add the original value
    sb.append(value);
    
    // Add a line break
    sb.append("<br>\n");
    
    // Return the generated String
    return (sb.toString());
  }
  
  
  /**
   * Builds an HTML string representation of this object.
   * 
   * @param drink the drink to generate as an HTML string
   * @return an HTML string representation of this object
   */
  public static String toHtmlString(final DrinkNode drink)
  {
    // Declare the string builder, used to build the output string
    StringBuilder sb = new StringBuilder(1000);
    
    // Build the output string
    sb.append("<html><body>");
    sb.append(buildHtmlSnippet("Name", drink.getName()));
    sb.append(buildHtmlSnippet("Glass", drink.getGlassString()));
    sb.append(buildHtmlSnippet("Type", drink.getCategoryString()));
    sb.append(buildHtmlSnippet("Alcohol Type", drink.getAlcoholString()));
    sb.append(buildHtmlSnippet("Instructions", drink.getInstructions()));
    
    List<String> ings = drink.getIngredients();
    if ((ings != null) && (ings.size() > 0))
    {
      final int size = ings.size();
      sb.append("<br>\n<b>Ingredients:</b>");
      for (int i = 0; i < size; ++i)
      {
        sb.append("<br>\n").append(ings.get(i)).append("\n");
      }
    }
    sb.append("</body></html>");
    
    return sb.toString();
  }
  
  
  /**
   * Return a description of the alcohol.
   * 
   * @param nAlcohol the alcohol type
   * @return a string description
   */
  public static String getAlcoholString(final int nAlcohol)
  {
    switch (nAlcohol)
    {
      case 0: return null;
      case 1: return "Alcoholic";
      case 2: return "Optional alcohol";
      case 3: return "Non alcoholic";
      default: return null;
    }
  }
  
  
  /**
   * Return the ID for the alcohol type, based
   * on the description.
   * 
   * @param sAlcohol the description
   * @return the alcohol ID
   */
  public static int getAlcoholForDesc(final String sAlcohol)
  {
    if (sAlcohol == null)
    {
      return 0;
    }
    else if (sAlcohol.equals("Alcoholic"))
    {
      return 1;
    }
    else if (sAlcohol.equals("Optional alcohol"))
    {
      return 2;
    }
    else if (sAlcohol.equals("Non alcoholic"))
    {
      return 3;
    }
    
    return 0;
  }
  
  
  /**
   * Return the description for the category with this ID.
   * 
   * @param id the category
   * @return description of the category
   */
  public static String getCategoryString(final int id)
  {
    switch (id)
    {
      case 0: return null;
      case 1: return "Milk / Float / Shake";
      case 2: return "Cocktail";
      case 3: return "Beer";
      case 4: return "Shot";
      case 5: return "Other/Unknown";
      case 6: return "Soft Drink / Soda";
      case 7: return "Punch / Party Drink";
      case 8: return "Homemade Liqueur";
      case 9: return "Cocoa";
      case 10: return "Ordinary Drink";
      case 11: return "Coffee / Tea";
      default: return null;
    }
  }
  
  
  /**
   * Return the category for the description.
   * 
   * @param desc the description
   * @return the corresponding ID
   */
  public static int getCategoryForDesc(final String desc)
  {
    if (desc == null)
    {
      return 0;
    }
    else if (desc.equals("Milk / Float / Shake"))
    {
      return 1;
    }
    else if (desc.equals("Cocktail"))
    {
      return 2;
    }
    else if (desc.equals("Beer"))
    {
      return 3;
    }
    else if (desc.equals("Shot"))
    {
      return 4;
    }
    else if (desc.equals("Other/Unknown"))
    {
      return 5;
    }
    else if (desc.equals("Soft Drink / Soda"))
    {
      return 6;
    }
    else if (desc.equals("Punch / Party Drink"))
    {
      return 7;
    }
    else if (desc.equals("Homemade Liqueur"))
    {
      return 8;
    }
    else if (desc.equals("Cocoa"))
    {
      return 9;
    }
    else if (desc.equals("Ordinary Drink"))
    {
      return 10;
    }
    else if (desc.equals("Coffee / Tea"))
    {
      return 11;
    }
    
    // Return the default value
    return 0;
  }
  
  
  /**
   * Return the description for the glass type with this ID.
   * 
   * @param id the type of glass
   * @return description of the glass
   */
  public static String getGlassString(final int id)
  {
    switch (id)
    {
      case 0: return null;
      case 1: return "Coffee mug";
      case 2: return "Pousse cafe glass";
      case 3: return "Irish coffee cup";
      case 4: return "Brandy snifter";
      case 5: return "Sherry glass";
      case 6: return "Red wine glass";
      case 7: return "Champagne flute";
      case 8: return "Beer mug";
      case 9: return "Cocktail glass";
      case 10: return "Unknown glass type";
      case 11: return "Pint glass";
      case 12: return "Hurricane glass";
      case 13: return "Pitcher";
      case 14: return "Beer pilsner";
      case 15: return "Mason jar";
      case 16: return "Whiskey sour glass";
      case 17: return "Punch bowl";
      case 18: return "Shot glass";
      case 19: return "Highball glass";
      case 20: return "Cordial glass";
      case 21: return "Margarita/Coupette glass";
      case 22: return "Collins glass";
      case 23: return "White wine glass";
      case 24: return "Old-fashioned glass";
      case 25: return "Parfait glass";
      default: return null;
    }
  }
  
  
  /**
   * Return the glass ID for the description.
   * 
   * @param desc the description
   * @return the corresponding ID
   */
  public static int getGlassForDesc(final String desc)
  {
    if (desc == null)
    {
      return 0;
    }
    else if (desc.equals("Coffee mug"))
    {
      return 1;
    }
    else if (desc.equals("Pousse cafe glass"))
    {
      return 2;
    }
    else if (desc.equals("Irish coffee cup"))
    {
      return 3;
    }
    else if (desc.equals("Brandy snifter"))
    {
      return 4;
    }
    else if (desc.equals("Sherry glass"))
    {
      return 5;
    }
    else if (desc.equals("Red wine glass"))
    {
      return 6;
    }
    else if (desc.equals("Champagne flute"))
    {
      return 7;
    }
    else if (desc.equals("Beer mug"))
    {
      return 8;
    }
    else if (desc.equals("Cocktail glass"))
    {
      return 9;
    }
    else if (desc.equals("Unknown glass type"))
    {
      return 10;
    }
    else if (desc.equals("Pint glass"))
    {
      return 11;
    }
    else if (desc.equals("Hurricane glass"))
    {
      return 12;
    }
    else if (desc.equals("Pitcher"))
    {
      return 13;
    }
    else if (desc.equals("Beer pilsner"))
    {
      return 14;
    }
    else if (desc.equals("Mason jar"))
    {
      return 15;
    }
    else if (desc.equals("Whiskey sour glass"))
    {
      return 16;
    }
    else if (desc.equals("Punch bowl"))
    {
      return 17;
    }
    else if (desc.equals("Shot glass"))
    {
      return 18;
    }
    else if (desc.equals("Highball glass"))
    {
      return 19;
    }
    else if (desc.equals("Cordial glass"))
    {
      return 20;
    }
    else if (desc.equals("Margarita/Coupette glass"))
    {
      return 21;
    }
    else if (desc.equals("Collins glass"))
    {
      return 22;
    }
    else if (desc.equals("White wine glass"))
    {
      return 23;
    }
    else if (desc.equals("Old-fashioned glass"))
    {
      return 24;
    }
    else if (desc.equals("Parfait glass"))
    {
      return 25;
    }
    
    // Return the default value
    return 0;
  }
}
