package problem2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author melika barzegaran hosseini
 */
public class Problem2
{
    private String path;
    private boolean detailedPrinting;

    public Problem2(String path)
    {
        this.path = path;
        detailedPrinting = false;
    }

    public void enableDetailedPrinting()
    {
        detailedPrinting = true;
    }

    public void disableDetailedPrinting()
    {
        detailedPrinting = false;
    }

    private Float[] read(String path)
    {
        if(path == null || path.isEmpty())
        {
            System.err.println("error: the path provided is null or empty.");
            System.exit(1);
        }

        BufferedReader reader = null;
        Float[] input = null;
        try
        {
            reader = new BufferedReader(new FileReader(path));

            String line;
            Integer n;
            if((line = reader.readLine()) != null && !line.isEmpty())
            {
                try
                {
                    n = Integer.parseInt(line);
                    if(n <= 0)
                    {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e)
                {
                    throw new Exception("error: the first line of the file '" + path + "' is not structured properly" +
                            ".\n it must represent the amount of data provided.\n it must be a positive integer " +
                            "number with the max value of '" + Integer.MAX_VALUE + "'.");
                }
            }
            else
            {
                throw new Exception("error: the first line of the file '" + path + "' is null or empty.\n it must " +
                        "represent the amount of data provided.");
            }

            if(detailedPrinting)
            {
                System.out.println("the amount of data provided = " + n);
            }

            input = new Float[n];

            for(int i = 0; i < n; i++)
            {
                if((line = reader.readLine()) != null && !line.isEmpty())
                {
                    String[] tokens = line.trim().split("\\s+");
                    if(tokens.length == 2)
                    {
                        try
                        {
                            if(Integer.parseInt(tokens[0]) != (i + 1))
                            {
                                throw new NumberFormatException();
                            }

                            input[i] = Float.parseFloat(tokens[1]);
                        }
                        catch (NumberFormatException e)
                        {
                            throw new Exception("error: the '" + (i + 2) + "'th line of the file '" + path + "' is " +
                                    "not structured properly.\n it must represent some data in the form of x and f(x)" +
                                    ".\n x must be a positive integer number from 1 to n.\n f(x) must be a float " +
                                    "number with the min value of '" + Float.MIN_VALUE + "' and the max value of '" +
                                    Float.MAX_VALUE + "'.");
                        }
                    }
                    else
                    {
                        throw new Exception("error: the '" + (i + 2) + "'th line of the file '" + path + "' is not " +
                                "structured properly.\n it must represent some data in the form of x and f(x).");
                    }
                }
                else
                {
                    throw new Exception("error: the '" + (i + 2) + "'th line of the file '" + path + "' is null or " +
                            "empty.\n it must represent some data.");
                }
            }

            if(detailedPrinting)
            {
                StringBuilder log = new StringBuilder("the sequence = ");
                for(Float value : input)
                {
                    log.append(value).append(", ");
                }
                log.setLength(log.length() - 2);
                System.out.println(log);
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("error: the file '" + path + "' doesn't exist or is a directory.");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("error: an error occurred while reading from the file '" + path + "'.");
            System.exit(1);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        finally
        {
            try
            {
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException e)
            {
                System.err.println("error: an error occurred while closing the file '" + path + "'.");
                System.exit(1);
            }
        }

        return input;
    }

    //returns the number of minimum changes which must be made in order to make the array input non decreasing
    private Integer solve(Float[] input)
    {
        //n represents the input size
        int n = input.length;

        //lis[i] represents the length of the longest increasing sub-sequence till index i
        int[] lis = new int[input.length];

        //initializing the lis array for all indexes
        for(int i = 0; i < n; i++)
            lis[i] = 1;

        //filling the lis array in a bottom up manner
        //using the recursive relation solve(i) = 1 + max{solve(j)} where i > j and input[i] >= input[j]
        //and if there is no such j then solve(i) = 1
        for(int i = 0; i < n; i++)
            for(int j = 0; j < i; j++)
                if(input[i] >= input[j] && lis[i] < lis[j] + 1)
                    lis[i] = lis[j] + 1;

        //picking the maximum value in the lis array
        int max = 0;
        for(int i = 0; i < n; i++)
            if(max < lis[i])
                max = lis[i];

        //returning the minimum of changes which must be made
        return n - max;
    }

    public void solve()
    {
        Float[] input = read(path);
        Integer output = solve(input);
        if(detailedPrinting)
        {
            System.out.println("the output = " + output);
        }
        else
        {
            System.out.print(output);
        }
    }

    public static void main(String args[])
    {
        if(args.length == 1)
        {
            Problem2 problem = new Problem2(args[0]);
            problem.enableDetailedPrinting();
            problem.solve();
        }
        else
        {
            System.err.println("error: the input is invalid. it should be the path of the input file.");
        }
    }
}