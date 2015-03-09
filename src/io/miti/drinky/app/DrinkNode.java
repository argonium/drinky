package io.miti.drinky.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulate a drink.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class DrinkNode implements Comparable<DrinkNode>, java.io.Serializable
{
  /**
   * The serial version number.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The name of the drink.
   */
  private String name;
  
  /**
   * The list of ingredients.
   */
  private List<String> ingredients = new ArrayList<String>(5);
  
  /**
   * Mixing instructions.
   */
  private String instructions;
  
  /**
   * The drink category.
   */
  private int category;
  
  /**
   * The alcohol category.
   */
  private int alcohol;
  
  /**
   * The glass type.
   */
  private int glass;
  
  
  /**
   * Default constructor.
   */
  public DrinkNode()
  {
    super();
  }
  
  
  /**
   * Get the alcohol type.
   * 
   * @return the alcohol type
   */
  public int getAlcohol()
  {
    return alcohol;
  }
  
  
  /**
   * Get the alcohol type as a string.
   * 
   * @return the alcohol type
   */
  public String getAlcoholString()
  {
    return Utility.getAlcoholString(alcohol);
  }
  
  
  /**
   * @param sAlcohol the alcohol to set
   */
  public void setAlcohol(final String sAlcohol)
  {
    alcohol = Utility.getAlcoholForDesc(sAlcohol);
  }
  
  
  /**
   * Return the category.
   * 
   * @return the category
   */
  public int getCategory()
  {
    return category;
  }
  
  
  /**
   * Get the category as a string.
   * 
   * @return the category
   */
  public String getCategoryString()
  {
    return Utility.getCategoryString(category);
  }
  
  
  /**
   * Set the category.
   * 
   * @param sCategory the category to set
   */
  public void setCategory(final String sCategory)
  {
    category = Utility.getCategoryForDesc(sCategory);
  }
  
  
  /**
   * Get the glass ID.
   * 
   * @return the glass ID
   */
  public int getGlass()
  {
    return glass;
  }
  
  
  /**
   * Get the glass type as a string.
   * 
   * @return the glass type
   */
  public String getGlassString()
  {
    return Utility.getGlassString(glass);
  }
  
  
  /**
   * Set the glass ID, by description.
   * 
   * @param sGlass the glass description
   */
  public void setGlass(final String sGlass)
  {
    glass = Utility.getGlassForDesc(sGlass);
  }
  
  
  /**
   * Get the list of ingredients.
   * 
   * @return the ingredients
   */
  public List<String> getIngredients()
  {
    return ingredients;
  }
  
  
  /**
   * Return the list of ingredients as a single string.
   * 
   * @return the ingredients as a string
   */
  public String getIngredientsString()
  {
    // Check the ingredients
    if ((ingredients == null) || (ingredients.size() < 1))
    {
      // No ingredients
      return "";
    }
    else if (ingredients.size() == 1)
    {
      // Just one ingredient, so return it
      return ingredients.get(0);
    }
    
    // Return the list of ingredients as a single string
    StringBuilder sb = new StringBuilder(200);
    sb.append(ingredients.get(0));
    for (int i = 1; i < ingredients.size(); ++i)
    {
      // Append a space and the next ingredient
      sb.append(' ').append(ingredients.get(i));
    }
    
    // Return the generated string
    return sb.toString();
  }
  
  
  /**
   * Add an ingredient.
   * 
   * @param ing the new ingredient
   */
  public void addIngredient(final String ing)
  {
    ingredients.add(ing);
  }
  
  
  /**
   * Set the ingredients.
   * 
   * @param ings the ingredients to set
   */
  public void setIngredients(final List<String> ings)
  {
    ingredients = ings;
  }
  
  
  /**
   * Return the instructions.
   * 
   * @return the instructions
   */
  public String getInstructions()
  {
    return instructions;
  }
  
  
  /**
   * Set the mixing instructions.
   * 
   * @param sInstructions the instructions to set
   */
  public void setInstructions(final String sInstructions)
  {
    instructions = sInstructions;
  }
  
  
  /**
   * Return the name.
   * 
   * @return the name
   */
  public String getName()
  {
    return name;
  }
  
  
  /**
   * Set the name of the drink.
   * 
   * @param sName the new name
   */
  public void setName(final String sName)
  {
    name = sName;
  }
  
  
  /**
   * Generate a string for this object.
   * 
   * @return this object as a string
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder(200);
    
    sb.append("Name: ").append(name)
      .append("\nIngredients: ").append(Integer.toString(ingredients.size()));
    
    for (int i = 0; i < ingredients.size(); ++i)
    {
      sb.append("\n  ").append(ingredients.get(i));
    }
    
    sb.append("\nMixing: ").append(instructions)
      .append("\nCategory: ").append(category)
      .append("\nAlcohol? ").append(alcohol)
      .append("\nGlass: ").append(glass);
    
    return sb.toString();
  }
  
  
  /**
   * Compare two strings.
   * 
   * @param s1 the first string
   * @param s2 the second string
   * @return the result of the comparison
   */
  private static int compareStrings(final String s1, final String s2)
  {
    if ((s1 == null) && (s2 == null))
    {
      return 0;
    }
    else if (s1 == null)
    {
      return -1;
    }
    else if (s2 == null)
    {
      return 1;
    }
    
    return s1.compareTo(s2);
  }
  
  
  /**
   * Compare a drink to this.
   * 
   * @param drink the drink to compare to this
   * @return the result of the comparison
   */
  public int compareTo(final DrinkNode drink)
  {
    // Compare the name
    int r = compareStrings(name, drink.getName());
    if (r != 0)
    {
      return r;
    }
    
    // Compare the instructions
    r = compareStrings(instructions, drink.getInstructions());
    if (r != 0)
    {
      return r;
    }
    
    // They must be equal
    return 0;
  }
}
