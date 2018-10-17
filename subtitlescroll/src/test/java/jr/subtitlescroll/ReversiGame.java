package jr.subtitlescroll;
//最新更新时间2018年10月4日 星期四晚上21:45
import java.util.*;

public class ReversiGame {
    public static void main(String[] args) {
		System.out.println("Hello everyone let us play the game ok?!    do it!");
		System.out.println(" ");
		Scanner input = new Scanner(System.in);

		int[][] Saving_pattern = new int[6][6];
        
		Saving_pattern[2][2] = 1;
		Saving_pattern[2][3] = 2;
		Saving_pattern[3][2] = 2;
		Saving_pattern[3][3] = 1;
		/*Saving_pattern[1][3] = 2;
		Saving_pattern[2][4] = 2;
		Saving_pattern[2][5] = 1;
		Saving_pattern[4][2] = 2;
		Saving_pattern[4][4] = 1;
		Saving_pattern[2][1] = 2;
		Saving_pattern[2][0] = 1;
		Saving_pattern[1][1] = 2;
		Saving_pattern[0][0] = 1;
		Saving_pattern[1][2] = 2;
	//	Saving_pattern[0][2] = 1;
		Saving_pattern[0][4] = 1;
		Saving_pattern[3][1] = 2;
		Saving_pattern[4][0] = 1;
		Saving_pattern[3][0] = 2;
		//Saving_pattern[5][2] = 1;
		Saving_pattern[0][3] = 2; */
		
		int Chess_Setting_time = 0;
		
		do {
		Runtime(Saving_pattern); // runtime means play game 
	    Type_One(Saving_pattern, input); // Enter the position of "1"
	    Chess_Setting_time ++;
		Runtime(Saving_pattern);
		Type_Two(Saving_pattern, input); // Enter the position of "2"
		Chess_Setting_time ++;
	}while(Chess_Setting_time <= 6*Saving_pattern.length - 4);
		
	}

	
			
		
	public static void Runtime(int Saving_pattern[][]) {
		System.out.println("    0  1  2  3  4  5");
		System.out.println("    ----------------");

		for (int list = 0; list < Saving_pattern.length; list++) { // i means the layer of Saving_pattern
			System.out.print(list + " ");
			System.out.print("| ");
			for (int row = 0; row < Saving_pattern[list].length; row++) { // c means the element of each layer

				System.out.print(Saving_pattern[list][row]);
				System.out.print("  ");
			}
			System.out.println();

		}
	}

	public static void Type_One(int Saving_pattern[][], Scanner input) {
		System.out.print("Please enter the psition of '1' :");
		int list, row;
		boolean past;
	
		do {
			list = input.nextInt(); // c means the element of each layer
			row = input.nextInt();// i means the layer of Saving_pattern

			if (list < 0 || row < 0 || 5 < list || 5 < row) {
				System.out.println("Error - Input numbers should be 0 to 5");
				System.out.println("Please input again:");
				past = false;
			} else if (Saving_pattern[list][row] == 1 || Saving_pattern[list][row] == 2) {
				System.out.println("Error - Input cell is not empty");
				System.out.println("Please input again:");
				past = false;
			} else {
				boolean chess_one_past = eat_chessTwo(list, row, Saving_pattern); //eat_chessTwo means transfer the chess 2 to 1
				if(chess_one_past == false) {
					past = false;
					System.out.println("Error - invalid move");
					System.out.println("Please input again");}
					else {
						past = true;
				}
			}
		} while (past == false);
		Saving_pattern[list][row] = 1;
		return;
	}

	public static void Type_Two(int Saving_pattern[][], Scanner input) {
		System.out.print("Please enter the position of '2' :");
		int list, row;
		boolean past;
	
		do {
			list = input.nextInt(); // c means the element of each layer
			row = input.nextInt();// i means the layer of Saving_pattern

			if (list < 0 || row < 0 || 5 < list || 5 < row) {
				System.out.println("Error - Input numbers should be 0 to 5");
				System.out.println("Please input again:");
				past = false;
			} else if (Saving_pattern[list][row] == 1 || Saving_pattern[list][row] == 2) {
				System.out.println("Error - Input cell is not empty");
				System.out.println("Please input again:");
				past = false;
			} else {
				boolean chess_one_past = eat_chessOne(list, row, Saving_pattern);  //chess_one_past应该改为chess_two_past
				if(chess_one_past == false) {   //
					past = false;
					System.out.println("Error - invalid move");
					System.out.println("Please input again");}
					else {
						past = true;
				}
			}
		} while (past == false);
		Saving_pattern[list][row] = 2;
		return;
		
		
		
	}
	
	public static boolean eat_chessTwo(int list,int row,int Saving_pattern[][]) {
      int Assumption_point1 = 0;
      int Assumption_point2 = 0;
      int Assumption_point3 = 0;
      int Assumption_point4 = 0;
      int Assumption_point5 = 0;
      int Assumption_point6 = 0;
      int Assumption_point7 = 0;
      int Assumption_point8 = 0;
      
      int test_all_zero = 0;   //if the value of test_all_zero is still zero after this the method of eat_chess,that meats all there are not 2 beside the chess,or there are not 1 after 2  
      
      if(0 <= (list + 1) && (list + 1) <= 5 && 0 <= (row + 1) && (row + 1) <= 5) { // check the southeast direction   
    	  
                  	  if(Saving_pattern[list + 1][row + 1] == 2) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
    		 
                  		if(( 0 <= list + 2 + Assumption_point1 ) && (list + 2 + Assumption_point1 <= 5) && (0 <= row + 2 + Assumption_point1) && (row + 2 + Assumption_point1 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
                  		  
    		  if(Saving_pattern[list + 2 + Assumption_point1][row + 2 + Assumption_point1] == 2) { //if the first chess is 2 do the operation inside the bracket of this if
            	  while(Saving_pattern[list + 2 + Assumption_point1][row + 2 + Assumption_point1] == 2) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
            		  
            		  Assumption_point1++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
            		  if((list + 2 + Assumption_point1 < 0) || (list + 2 + Assumption_point1 > 5) || (row + 2 + Assumption_point1 < 0) || (row + 2 + Assumption_point1 > 5)) {
                    	 
                    	  break; //we can use break over here since now we are inside the while loop
            		  }
                    if(Saving_pattern[list + 2 + Assumption_point1][row + 2 + Assumption_point1] == 1) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
                    	while(Saving_pattern[list + 1 + Assumption_point1][row + 1 + Assumption_point1] == 2) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
                    		Saving_pattern[list + 1 + Assumption_point1][row + 1 + Assumption_point1] = 1;
                                Assumption_point1--;
                                test_all_zero ++; //check the above explanation
                    	}
            	      }
    	            }
    		      }
    		  if(Saving_pattern[list + 2 + Assumption_point1][row + 2 + Assumption_point1] == 1) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
    			  Saving_pattern[list + 1 + Assumption_point1][row + 1 + Assumption_point1] = 1;
    			  test_all_zero ++; //check the above explanation
    		  }
    		  }
    		   }
           }// This bracket is to end the "if" condition that check the southeast direction 
        
      
      if(0 <= (list + 1) && (list + 1) <= 5) {//south direction
    	  
      
      if(Saving_pattern[list + 1][row] == 2) { //this Saving_pattern[list][row] belongs to the first chess 2 of east and south direction
 		 
    	  if(( 0 <= list + 2 + Assumption_point2 ) && (list + 2 + Assumption_point2 <= 5) && (0 <= row) && (row <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
    	  
		  if(Saving_pattern[list + 2 + Assumption_point2][row] == 2) { //if the first chess is 2 do the operation inside the bracket of this if
        	  while(Saving_pattern[list + 2 + Assumption_point2][row] == 2) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
        		  
        		  Assumption_point2++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
        		  if((list + 2 + Assumption_point2 < 0 ) || ((list + 2 + Assumption_point2) > 5) || (row < 0) || (row > 5) ) {
                	 
                	  break; //we can use break over here since now we are inside the while loop
        		  }
                if(Saving_pattern[list + 2 + Assumption_point2][row] == 1) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
                	while(Saving_pattern[list + 1 + Assumption_point2][row] == 2) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
                		Saving_pattern[list + 1 + Assumption_point2][row] = 1;
                            Assumption_point2--;
                            test_all_zero ++; //check the above explanation
                	}
        	      }
	            }
		      }
		  if(Saving_pattern[list + 2 + Assumption_point2][row] == 1) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
			  Saving_pattern[list + 1 + Assumption_point2][row] = 1;
			  test_all_zero ++; //check the above explanation
		  }
    	  }
    	  }
      }// This bracket is to end the "if" condition that check the south direction 
    	  
    	  

      if(0 <= (list + 1) && (list + 1) <= 5 && 0 <= (row - 1) && (row - 1) <= 5) { // southwest direction
    	  
    	  if(Saving_pattern[list + 1][row - 1] == 2) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
     		 
    		  if(( 0 <= list + 2 + Assumption_point3 ) && (list + 2 + Assumption_point3 <= 5) && (0 <= row - 2 - Assumption_point3) && (row - 2 - Assumption_point1 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
    			  
    		  if(Saving_pattern[list + 2 + Assumption_point3][row - 2 - Assumption_point3] == 2) { //if the first chess is 2 do the operation inside the bracket of this if
            	  while(Saving_pattern[list + 2 + Assumption_point3][row - 2 - Assumption_point3] == 2) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
            		  
            		  Assumption_point3++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
            		  if((list + 2 + Assumption_point3 < 0) || (list + 2 + Assumption_point3 > 5) || (row - 2 - Assumption_point3 < 0) || (row - 2 - Assumption_point3 > 5)) {
            			  
                    	  break; //we can use break over here since now we are inside the while loop
            		  }
                    if(Saving_pattern[list + 2 + Assumption_point3][row - 2 - Assumption_point3] == 1) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
                    	while(Saving_pattern[list + 1 + Assumption_point3][row - 1 - Assumption_point3] == 2) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
                    		Saving_pattern[list + 1 + Assumption_point3][row - 1 - Assumption_point3] = 1;
                                Assumption_point3--;
                                test_all_zero ++; //check the above explanation
                    	}
            	      }
    	            }
    		      }
    		  if(Saving_pattern[list + 2 + Assumption_point3][row - 2 - Assumption_point3] == 1) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
    			  Saving_pattern[list + 1 + Assumption_point3][row - 1 - Assumption_point3] = 1;
    			  test_all_zero ++; //check the above explanation
    		   }
    	       }
             }	  
           }// This bracket is to end the "if" condition that check the southwest direction
    	  
    	  
      if(0 <= (row + 1) && (row + 1) <= 5) { //east direction
        	  
          	  if(Saving_pattern[list][row + 1] == 2) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
	 
          		if(( 0 <= list ) && (list <= 5) && (0 <= row + 2 + Assumption_point4) && (row + 2 + Assumption_point4 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
          		  
	  if(Saving_pattern[list][row + 2 + Assumption_point4] == 2) { //if the first chess is 2 do the operation inside the bracket of this if
    	  while(Saving_pattern[list][row + 2 + Assumption_point4] == 2) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
    		  
    		  Assumption_point4++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
    		  if((list < 0) || (list > 5) || (row + 2 + Assumption_point4 < 0) || (row + 2 + Assumption_point4 > 5)) {
            	 
            	  break; //we can use break over here since now we are inside the while loop
    		  }
            if(Saving_pattern[list][row + 2 + Assumption_point4] == 1) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
            	while(Saving_pattern[list][row + 1 + Assumption_point4] == 2) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
            		Saving_pattern[list][row + 1 + Assumption_point4] = 1;
                        Assumption_point4--;
                        test_all_zero ++; //check the above explanation
            	}
    	      }
            }
	      }
	  if(Saving_pattern[list][row + 2 + Assumption_point4] == 1) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
		  Saving_pattern[list][row + 1 + Assumption_point4] = 1;
		  test_all_zero ++; //check the above explanation
	   }
      }
     }	  
   }// This bracket is to end the "if" condition that check the east direction 

    	  
      if(0 <= (row - 1) && (row - 1) <= 5) { //west direction
    	  if(Saving_pattern[list][row - 1] == 2) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
     		 
        		if(( 0 <= list ) && (list <= 5) && (0 <= row - 2 - Assumption_point5) && (row - 2 - Assumption_point5 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
        		  
	  if(Saving_pattern[list][row - 2 - Assumption_point5] == 2) { //if the first chess is 2 do the operation inside the bracket of this if
  	  while(Saving_pattern[list][row - 2 - Assumption_point5] == 2) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
  		  
  		  Assumption_point5++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
  		  if((list < 0) || (list > 5) || (row - 2 - Assumption_point5 < 0) || (row - 2 - Assumption_point5 > 5)) {
          	 
          	  break; //we can use break over here since now we are inside the while loop
  		  }
          if(Saving_pattern[list][row - 2 - Assumption_point5] == 1) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
          	while(Saving_pattern[list][row - 1 - Assumption_point5] == 2) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
          		Saving_pattern[list][row - 1 - Assumption_point5] = 1;
                      Assumption_point5--;
                      test_all_zero ++; //check the above explanation
          	}
  	      }
          }
	      }
	  if(Saving_pattern[list][row - 2 - Assumption_point5] == 1) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
		  Saving_pattern[list][row - 1 - Assumption_point5] = 1;
		  test_all_zero ++; //check the above explanation
	  }
	  }
	   }
 }// This bracket is to end the "if" condition that check the west direction 
      
      if(0 <= (list - 1) && (list - 1) <= 5 && 0 <= (row + 1) && (row + 1) <= 5) { // northeast direction 
    	  if(Saving_pattern[list - 1][row + 1] == 2) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
     		 
        		if(( 0 <= list - 2 - Assumption_point6 ) && (list - 2 - Assumption_point6 <= 5) && (0 <= row + 2 + Assumption_point6) && (row + 2 + Assumption_point6 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
        		  
	  if(Saving_pattern[list - 2 - Assumption_point6][row + 2 + Assumption_point6] == 2) { //if the first chess is 2 do the operation inside the bracket of this if
  	  while(Saving_pattern[list - 2 - Assumption_point6][row + 2 + Assumption_point6] == 2) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
  		  
  		  Assumption_point6++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
  		  if((list - 2 - Assumption_point6 < 0) || (list - 2 - Assumption_point6 > 5) || (row + 2 + Assumption_point6 < 0) || (row + 2 + Assumption_point6 > 5)) {
          	 
          	  break; //we can use break over here since now we are inside the while loop
  		  }
          if(Saving_pattern[list - 2 - Assumption_point6][row + 2 + Assumption_point6] == 1) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
          	while(Saving_pattern[list - 1 - Assumption_point6][row + 1 + Assumption_point6] == 2) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
          		Saving_pattern[list - 1 - Assumption_point6][row + 1 + Assumption_point6] = 1;
                      Assumption_point6--;
                      test_all_zero ++; //check the above explanation
          	}
  	      }
          }
	      }
	  if(Saving_pattern[list - 2 - Assumption_point6][row + 2 + Assumption_point6] == 1) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
		  Saving_pattern[list - 1 - Assumption_point6][row + 1 + Assumption_point6] = 1;
		  test_all_zero ++; //check the above explanation
	  }
	  }
	   }
 }// This bracket is to end the "if" condition that check the northeast direction 
    	  
      if(0 <= (list + 1) && (list + 1) <= 5 ) {//north direction
    	  if(Saving_pattern[list + 1][row] == 2) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
     		 
        		if(( 0 <= list + 2 + Assumption_point7 ) && (list + 2 + Assumption_point7 <= 5) && (0 <= row) && (row <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
        		  
	  if(Saving_pattern[list + 2 + Assumption_point7][row] == 2) { //if the first chess is 2 do the operation inside the bracket of this if
  	  while(Saving_pattern[list + 2 + Assumption_point7][row] == 2) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
  		  
  		  Assumption_point7++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
  		  if((list + 2 + Assumption_point7 < 0) || (list + 2 + Assumption_point7 > 5) || (row < 0) || (row > 5)) {
          	 
          	  break; //we can use break over here since now we are inside the while loop
  		  }
          if(Saving_pattern[list + 2 + Assumption_point7][row] == 1) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
          	while(Saving_pattern[list + 1 + Assumption_point7][row] == 2) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
          		Saving_pattern[list + 1 + Assumption_point7][row] = 1;
                      Assumption_point7--;
                      test_all_zero ++; //check the above explanation
          	}
  	      }
          }
	      }
	  if(Saving_pattern[list + 2 + Assumption_point7][row] == 1) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
		  Saving_pattern[list + 1 + Assumption_point7][row] = 1;
		  test_all_zero ++; //check the above explanation
	  }
	  }
	   }
 }// This bracket is to end the "if" condition that check the north direction 
    	  
      if(0 <= (list - 1) && (list - 1) <= 5 && 0 <= (row - 1) && (row - 1) <= 5) {// northwest direction
    	  if(Saving_pattern[list - 1][row - 1] == 2) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
     		 
        		if(( 0 <= list - 2 - Assumption_point8 ) && (list - 2 - Assumption_point8 <= 5) && (0 <= row - 2 - Assumption_point8) && (row - 2 - Assumption_point8 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
        		  
	  if(Saving_pattern[list - 2 - Assumption_point8][row - 2 - Assumption_point8] == 2) { //if the first chess is 2 do the operation inside the bracket of this if
  	  while(Saving_pattern[list - 2 - Assumption_point8][row - 2 - Assumption_point8] == 2) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
  		  
  		  Assumption_point8++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
  		  if((list - 2 - Assumption_point8 < 0) || (list - 2 - Assumption_point8 > 5) || (row - 2 - Assumption_point8 < 0) || (row - 2 - Assumption_point8 > 5)) {
          	 
          	  break; //we can use break over here since now we are inside the while loop
  		  }
          if(Saving_pattern[list - 2 - Assumption_point8][row - 2 - Assumption_point8] == 1) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
          	while(Saving_pattern[list - 1 - Assumption_point8][row - 1 - Assumption_point8] == 2) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
          		Saving_pattern[list - 1 - Assumption_point8][row - 1 - Assumption_point8] = 1;
                      Assumption_point8--;
                      test_all_zero ++; //check the above explanation
          	}
  	      }
          }
	      }
	  if(Saving_pattern[list - 2 - Assumption_point8][row - 2 - Assumption_point8] == 1) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
		  Saving_pattern[list - 1 - Assumption_point8][row - 1 - Assumption_point8] = 1;
		  test_all_zero ++; //check the above explanation
	  }
	  }
	   }
 }// This bracket is to end the "if" condition that check the northwest direction 
    	  
		if (test_all_zero == 0) {
      //  boolean	chess_one_past = false;
        return false;
		}
		return true;
		
}//This bracket is for end the method of eat_chessTwo


	public static boolean eat_chessOne(int list,int row,int Saving_pattern[][]) {
		  int Assumption_point1 = 0;
	      int Assumption_point2 = 0;
	      int Assumption_point3 = 0;
	      int Assumption_point4 = 0;
	      int Assumption_point5 = 0;
	      int Assumption_point6 = 0;
	      int Assumption_point7 = 0;
	      int Assumption_point8 = 0;
	      
	      int test_all_zero = 0;   //if the value of test_all_zero is still zero after this the method of eat_chess,that meats all there are not 2 beside the chess,or there are not 1 after 2  
	      
	      if(0 <= (list + 1) && (list + 1) <= 5 && 0 <= (row + 1) && (row + 1) <= 5) { // check the southeast direction   
	    	  
	                  	  if(Saving_pattern[list + 1][row + 1] == 1) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
	    		 
	                  		if(( 0 <= list + 2 + Assumption_point1 ) && (list + 2 + Assumption_point1 <= 5) && (0 <= row + 2 + Assumption_point1) && (row + 2 + Assumption_point1 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
	                  		  
	    		  if(Saving_pattern[list + 2 + Assumption_point1][row + 2 + Assumption_point1] == 1) { //if the first chess is 2 do the operation inside the bracket of this if
	            	  while(Saving_pattern[list + 2 + Assumption_point1][row + 2 + Assumption_point1] == 1) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
	            		  
	            		  Assumption_point1++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
	            		  if((list + 2 + Assumption_point1 < 0) || (list + 2 + Assumption_point1 > 5) || (row + 2 + Assumption_point1 < 0) || (row + 2 + Assumption_point1 > 5)) {
	                    	 
	                    	  break; //we can use break over here since now we are inside the while loop
	            		  }
	                    if(Saving_pattern[list + 2 + Assumption_point1][row + 2 + Assumption_point1] == 2) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
	                    	while(Saving_pattern[list + 1 + Assumption_point1][row + 1 + Assumption_point1] == 1) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
	                    		Saving_pattern[list + 1 + Assumption_point1][row + 1 + Assumption_point1] = 2;
	                                Assumption_point1--;
	                                test_all_zero ++; //check the above explanation
	                    	}
	            	      }
	    	            }
	    		      }
	    		  if(Saving_pattern[list + 2 + Assumption_point1][row + 2 + Assumption_point1] == 2) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
	    			  Saving_pattern[list + 1 + Assumption_point1][row + 1 + Assumption_point1] = 2;
	    			  test_all_zero ++; //check the above explanation
	    		  }
	    		  }
	    		   }
	           }// This bracket is to end the "if" condition that check the southeast direction 
	        
	      
	      if(0 <= (list + 1) && (list + 1) <= 5) {//south direction
	    	  
	      
	      if(Saving_pattern[list + 1][row] == 1) { //this Saving_pattern[list][row] belongs to the first chess 2 of east and south direction
	 		 
	    	  if(( 0 <= list + 2 + Assumption_point2 ) && (list + 2 + Assumption_point2 <= 5) && (0 <= row) && (row <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
	    	  
			  if(Saving_pattern[list + 2 + Assumption_point2][row] == 1) { //if the first chess is 2 do the operation inside the bracket of this if
	        	  while(Saving_pattern[list + 2 + Assumption_point2][row] == 1) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
	        		  
	        		  Assumption_point2++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
	        		  if((list + 2 + Assumption_point2 < 0 ) || ((list + 2 + Assumption_point2) > 5) || (row < 0) || (row > 5) ) {
	                	 
	                	  break; //we can use break over here since now we are inside the while loop
	        		  }
	                if(Saving_pattern[list + 2 + Assumption_point2][row] == 2) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
	                	while(Saving_pattern[list + 1 + Assumption_point2][row] == 1) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
	                		Saving_pattern[list + 1 + Assumption_point2][row] = 2;
	                            Assumption_point2--;
	                            test_all_zero ++; //check the above explanation
	                	}
	        	      }
		            }
			      }
			  if(Saving_pattern[list + 2 + Assumption_point2][row] == 2) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
				  Saving_pattern[list + 1 + Assumption_point2][row] = 2;
				  test_all_zero ++; //check the above explanation
			  }
	    	  }
	    	  }
	      }// This bracket is to end the "if" condition that check the south direction 
	    	  
	    	  

	      if(0 <= (list + 1) && (list + 1) <= 5 && 0 <= (row - 1) && (row - 1) <= 5) { // southwest direction
	    	  
	    	  if(Saving_pattern[list + 1][row - 1] == 1) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
	     		 
	    		  if(( 0 <= list + 2 + Assumption_point1 ) && (list + 2 + Assumption_point1 <= 5) && (0 <= row - 2 - Assumption_point1) && (row - 2 - Assumption_point1 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
	    			  
	    		  if(Saving_pattern[list + 2 + Assumption_point3][row - 2 - Assumption_point3] == 1) { //if the first chess is 2 do the operation inside the bracket of this if
	            	  while(Saving_pattern[list + 2 + Assumption_point3][row - 2 - Assumption_point3] == 1) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
	            		  
	            		  Assumption_point3++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
	            		  if((list + 2 + Assumption_point3 < 0) || (list + 2 + Assumption_point3 > 5) || (row - 2 - Assumption_point3 < 0) || (row - 2 - Assumption_point3 > 5)) {
	            			  
	                    	  break; //we can use break over here since now we are inside the while loop
	            		  }
	                    if(Saving_pattern[list + 2 + Assumption_point3][row - 2 - Assumption_point3] == 2) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
	                    	while(Saving_pattern[list + 1 + Assumption_point3][row - 1 - Assumption_point3] == 1) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
	                    		Saving_pattern[list + 1 + Assumption_point3][row - 1 - Assumption_point3] = 2;
	                                Assumption_point3--;
	                                test_all_zero ++; //check the above explanation
	                    	}
	            	      }
	    	            }
	    		      }
	    		  if(Saving_pattern[list + 2 + Assumption_point3][row - 2 - Assumption_point3] == 2) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
	    			  Saving_pattern[list + 1 + Assumption_point3][row - 1 - Assumption_point3] = 2;
	    			  test_all_zero ++; //check the above explanation
	    		   }
	    	       }
	             }	  
	           }// This bracket is to end the "if" condition that check the southwest direction
	    	  
	    	  
	      if(0 <= (row + 1) && (row + 1) <= 5) { //east direction
	        	  
	          	  if(Saving_pattern[list][row + 1] == 1) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
		 
	          		if(( 0 <= list ) && (list <= 5) && (0 <= row + 2 + Assumption_point4) && (row + 2 + Assumption_point4 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
	          		  
		  if(Saving_pattern[list][row + 2 + Assumption_point4] == 1) { //if the first chess is 2 do the operation inside the bracket of this if
	    	  while(Saving_pattern[list][row + 2 + Assumption_point4] == 1) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
	    		  
	    		  Assumption_point4++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
	    		  if((list < 0) || (list > 5) || (row + 2 + Assumption_point4 < 0) || (row + 2 + Assumption_point4 > 5)) {
	            	 
	            	  break; //we can use break over here since now we are inside the while loop
	    		  }
	            if(Saving_pattern[list][row + 2 + Assumption_point4] == 2) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
	            	while(Saving_pattern[list][row + 1 + Assumption_point4] == 1) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
	            		Saving_pattern[list][row + 1 + Assumption_point4] = 2;
	                        Assumption_point4--;
	                        test_all_zero ++; //check the above explanation
	            	}
	    	      }
	            }
		      }
		  if(Saving_pattern[list][row + 2 + Assumption_point4] == 2) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
			  Saving_pattern[list][row + 1 + Assumption_point4] = 2;
			  test_all_zero ++; //check the above explanation
		   }
	      }
	     }	  
	   }// This bracket is to end the "if" condition that check the east direction 

	    	  
	      if(0 <= (row - 1) && (row - 1) <= 5) { //west direction
	    	  if(Saving_pattern[list][row - 1] == 1) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
	     		 
	        		if(( 0 <= list ) && (list <= 5) && (0 <= row - 2 - Assumption_point5) && (row - 2 - Assumption_point5 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
	        		  
		  if(Saving_pattern[list][row - 2 - Assumption_point5] == 1) { //if the first chess is 2 do the operation inside the bracket of this if
	  	  while(Saving_pattern[list][row - 2 - Assumption_point5] == 1) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
	  		  
	  		  Assumption_point5++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
	  		  if((list < 0) || (list > 5) || (row - 2 - Assumption_point5 < 0) || (row - 2 - Assumption_point5 > 5)) {
	          	 
	          	  break; //we can use break over here since now we are inside the while loop
	  		  }
	          if(Saving_pattern[list][row - 2 - Assumption_point5] == 2) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
	          	while(Saving_pattern[list][row - 1 - Assumption_point5] == 1) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
	          		Saving_pattern[list][row - 1 - Assumption_point5] = 2;
	                      Assumption_point5--;
	                      test_all_zero ++; //check the above explanation
	          	}
	  	      }
	          }
		      }
		  if(Saving_pattern[list][row - 2 - Assumption_point5] == 2) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
			  Saving_pattern[list][row - 1 - Assumption_point5] = 2;
			  test_all_zero ++; //check the above explanation
		  }
		  }
		   }
	 }// This bracket is to end the "if" condition that check the west direction 
	      
	      if(0 <= (list - 1) && (list - 1) <= 5 && 0 <= (row + 1) && (row + 1) <= 5) { // northeast direction 
	    	  if(Saving_pattern[list - 1][row + 1] == 1) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
	     		 
	        		if(( 0 <= list - 2 - Assumption_point6 ) && (list - 2 - Assumption_point6 <= 5) && (0 <= row + 2 + Assumption_point6) && (row + 2 + Assumption_point6 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
	        		  
		  if(Saving_pattern[list - 2 - Assumption_point6][row + 2 + Assumption_point6] == 1) { //if the first chess is 2 do the operation inside the bracket of this if
	  	  while(Saving_pattern[list - 2 - Assumption_point6][row + 2 + Assumption_point6] == 1) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
	  		  
	  		  Assumption_point6++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
	  		  if((list - 2 - Assumption_point6 < 0) || (list - 2 - Assumption_point6 > 5) || (row + 2 + Assumption_point6 < 0) || (row + 2 + Assumption_point6 > 5)) {
	          	 
	          	  break; //we can use break over here since now we are inside the while loop
	  		  }
	          if(Saving_pattern[list - 2 - Assumption_point6][row + 2 + Assumption_point6] == 2) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
	          	while(Saving_pattern[list - 1 - Assumption_point6][row + 1 + Assumption_point6] == 1) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
	          		Saving_pattern[list - 1 - Assumption_point6][row + 1 + Assumption_point6] = 2;
	                      Assumption_point6--;
	                      test_all_zero ++; //check the above explanation
	          	}
	  	      }
	          }
		      }
		  if(Saving_pattern[list - 2 - Assumption_point6][row + 2 + Assumption_point6] == 2) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
			  Saving_pattern[list - 1 - Assumption_point6][row + 1 + Assumption_point6] = 2;
			  test_all_zero ++; //check the above explanation
		  }
		  }
		   }
	 }// This bracket is to end the "if" condition that check the northeast direction 
	    	  
	      if(0 <= (list + 1) && (list + 1) <= 5 ) {//north direction
	    	  if(Saving_pattern[list + 1][row] == 1) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
	     		 
	        		if(( 0 <= list + 2 + Assumption_point7 ) && (list + 2 + Assumption_point7 <= 5) && (0 <= row) && (row <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
	        		  
		  if(Saving_pattern[list + 2 + Assumption_point7][row] == 1) { //if the first chess is 2 do the operation inside the bracket of this if
	  	  while(Saving_pattern[list + 2 + Assumption_point7][row] == 1) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
	  		  
	  		  Assumption_point7++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
	  		  if((list + 2 + Assumption_point7 < 0) || (list + 2 + Assumption_point7 > 5) || (row < 0) || (row > 5)) {
	          	 
	          	  break; //we can use break over here since now we are inside the while loop
	  		  }
	          if(Saving_pattern[list + 2 + Assumption_point7][row] == 2) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
	          	while(Saving_pattern[list + 1 + Assumption_point7][row] == 1) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
	          		Saving_pattern[list + 1 + Assumption_point7][row] = 2;
	                      Assumption_point7--;
	                      test_all_zero ++; //check the above explanation
	          	}
	  	      }
	          }
		      }
		  if(Saving_pattern[list + 2 + Assumption_point7][row] == 2) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
			  Saving_pattern[list + 1 + Assumption_point7][row] = 2;
			  test_all_zero ++; //check the above explanation
		  }
		  }
		   }
	 }// This bracket is to end the "if" condition that check the north direction 
	    	  
	      if(0 <= (list - 1) && (list - 1) <= 5 && 0 <= (row - 1) && (row - 1) <= 5) {// northwest direction
	    	  if(Saving_pattern[list - 1][row - 1] == 1) { //this Saving_pattern[list][row] belongs to the first chess 2 of southeast direction
	     		 
	        		if(( 0 <= list - 2 - Assumption_point8 ) && (list - 2 - Assumption_point8 <= 5) && (0 <= row - 2 - Assumption_point8) && (row - 2 - Assumption_point8 <= 5)) {//The date inside this "if" will be ran if this "if" condition is satisfied
	        		  
		  if(Saving_pattern[list - 2 - Assumption_point8][row - 2 - Assumption_point8] == 1) { //if the first chess is 2 do the operation inside the bracket of this if
	  	  while(Saving_pattern[list - 2 - Assumption_point8][row - 2 - Assumption_point8] == 1) { //If the chess is still 2 after chess 2 ,that will continue to ratiocinate the next chess that belongs to the same direction
	  		  
	  		  Assumption_point8++;//this is very important since it can let the while  loop to judge if the next array is 2 or 1，if the value of next array is one,it will stop the while loop 
	  		  if((list - 2 - Assumption_point8 < 0) || (list - 2 - Assumption_point8 > 5) || (row - 2 - Assumption_point8 < 0) || (row - 2 - Assumption_point8 > 5)) {
	          	 
	          	  break; //we can use break over here since now we are inside the while loop
	  		  }
	          if(Saving_pattern[list - 2 - Assumption_point8][row - 2 - Assumption_point8] == 2) {  //if Saving_pattern[list + 1 + Assumption_point][row + 1 + Assumption_point] == 1 is not right, the 
	          	while(Saving_pattern[list - 1 - Assumption_point8][row - 1 - Assumption_point8] == 1) { //the reason why the number "1" disappear is because we need to decide if we need to change the last array value 2 to 1. Also, because we input the chess 1 at the begging,therefore it will stop this while loop when this while loop check the array value is 1
	          		Saving_pattern[list - 1 - Assumption_point8][row - 1 - Assumption_point8] = 2;
	                      Assumption_point8--;
	                      test_all_zero ++; //check the above explanation
	          	}
	  	      }
	          }
		      }
		  if(Saving_pattern[list - 2 - Assumption_point8][row - 2 - Assumption_point8] == 2) { //if the the second chess after setting chess is 1, that we can transfer the chess 2 to 1 
			  Saving_pattern[list - 1 - Assumption_point8][row - 1 - Assumption_point8] = 2;
			  test_all_zero ++; //check the above explanation
		  }
		  }
		   }
	 }// This bracket is to end the "if" condition that check the northwest direction 
	    	  
			if (test_all_zero == 0) {
	      //  boolean	chess_one_past = false;
	        return false;
			}
			return true;
			
	}//This bracket is for end the method of eat_chess
		
	}


