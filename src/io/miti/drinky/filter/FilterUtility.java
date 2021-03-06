package io.miti.drinky.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provide some helper methods for the filters.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FilterUtility
{
  /**
   * Default constructor.
   */
  private FilterUtility()
  {
    super();
  }
  
  
  /**
   * Write out the strings in a list.
   * 
   * @param list the list of strings to print
   */
  public static void writeList(final List<String> list)
  {
    // Check for null or empty
    if (list == null)
    {
      System.out.println("The list is null.");
    }
    else if (list.size() < 1)
    {
      System.out.println("The list is empty");
    }
    
    int size = list.size();
    for (int i = 0; i < size; ++i)
    {
      System.out.println("#" + Integer.toString(i + 1) + ": " + list.get(i));
    }
  }
  
  
  /**
   * Write out the hashmap to standard out.
   * 
   * @param data the hashmap to write
   */
  public static void writeHashMap(final HashMap<String, Integer> data)
  {
    // Check the input
    if (data == null)
    {
      System.out.println("The hashmap is null.");
      return;
    }
    
    // Iterate over the list of strings and write them out,
    // along with the number of occurrences of each
    for (String s : data.keySet())
    {
      System.out.println(s + ": " + data.get(s));
    }
  }
  
  
  /**
   * Parse the string into a list of phrases.  A phrase
   * is either a standalone word from the input string,
   * or a quoted string.
   * 
   * @param sInputTerm the string to parse
   * @return the list of phrases in the word
   */
  public static List<String> parseIntoPhrases(final String sInputTerm)
  {
    // Check the input
    if ((sInputTerm == null) || (sInputTerm.length() < 1))
    {
      // Return an empty list
      return new ArrayList<String>(0);
    }
    
    // Allocate an array to hold the items
    List<String> list = new ArrayList<String>(20);
    
    // Trim the string
    final String sInput = sInputTerm.trim();
    if (sInput.length() < 1)
    {
      // Return an empty list
      return new ArrayList<String>(0);
    }
    
    // Build the list
    final int nLen = sInput.length();
    boolean inQuote = false;
    int i = 0;
    StringBuilder sb = new StringBuilder(200);
    while (i < nLen)
    {
      // Save the character
      char ch = sInput.charAt(i);
      
      // Check for a leading quote
      if (ch == '"')
      {
        // See if we're already inside a quote
        if (inQuote)
        {
          list.add(sb.toString());
        }
        
        inQuote = !inQuote;
        sb.setLength(0);
        ++i;
      }
      else if (ch == '\\')
      {
        // Go to the next character
        ++i;
        
        // See if we're at the end
        if (i >= nLen)
        {
          break;
        }
        else
        {
          ch = sInput.charAt(i);
          switch (ch)
          {
            case 'n': sb.append('\n');
                      break;
            case 't': sb.append('\t');
                      break;
            case 'r': sb.append('\r');
                      break;
            default : sb.append(ch);
                      break;
          }
        }
        
        ++i;
      }
      else if (ch == ' ')
      {
        // See if we're in quotes
        if (inQuote)
        {
          // Save the space
          sb.append(ch);
        }
        else
        {
          // Break on spaces, so save the current word, if
          // there is one
          if (sb.length() > 0)
          {
            list.add(sb.toString());
            sb.setLength(0);
          }
        }
        
        ++i;
      }
      else
      {
        // Just add the character
        sb.append(ch);
        ++i;
      }
    }
    
    // Check if sb has any leftover strings
    if (sb.length() > 0)
    {
      list.add(sb.toString());
    }
    
    // Return the list
    return list;
  }
}
