package www.hailong.com.nsum;




import org.junit.Test;

import java.util.HashMap;

/**
 * nSUM问题是指，在一个数组中，找出n个数相加和等于给定的数，这个叫做nSUM问题。
 */
public class nSumDemo {

    /**
     * 2Sum问题的解决
     * 1,把问题的结果放入一个HashMap当中就可以吧
     */
    public int[] TwoSum(int nums[],int target){
        //进行把所有数组当中的数据都放入我们HashMap当中，做一个映射就可以
        HashMap<Integer,Integer> map=new HashMap<Integer, Integer>();
        for (int i=0;i<nums.length;i++){
                map.put(nums[i],i);//记录我们值的位置
        }
        //定义二个数组，用来存储我们目标数据
        int res[]=new int[2];
        for(int j=0;j<nums.length;j++){
            //进行做差,
            int key=target-nums[j];
            //map.get(key)!=j说明这个key不是同一个数字
            if(map.containsKey(key)&&map.get(key)!=j){
                res[0]=map.get(key);
                res[1]=j;
            }
        }
        return res;
    }
    /**
     * 使用一个For来搞定这个
     * @param nums
     * @param target
     * @return
     */
    public int[] TwoSum2(int nums[],int target){
        //进行把所有数组当中的数据都放入我们HashMap当中，做一个映射就可以
        HashMap<Integer,Integer> map=new HashMap<Integer, Integer>();
        //定义二个数组，用来存储我们目标数据
        int res[]=new int[2];
        for(int j=0;j<nums.length;j++){
            //进行做差,
            int key=target-nums[j];
            //map.get(key)!=j说明这个key不是同一个数字
            if(map.containsKey(key)&&map.get(key)!=j){
                res[0]=map.get(key);
                res[1]=j;
            }
            map.put(nums[j],j);
        }
        return res;
    }
    /**
     * 主要是用来进行测试我们的2Sum
     */
    @Test
    public void test(){
        int nums[]={4,6,3,9};
        int two[]=TwoSum2(nums,9);
        for (int i = 0; i <2; i++) {
            System.out.println("two:"+two[i]);
        }
    }
    /**
     * 我们的输入数组有序，也是算我们的2Sum问题
     * 因为有序可以用二分法来做
     */
    public int[] TwoSumSorted(int nums[],int target){
        //定义二个数组，用来存储我们目标数据
        int res[]=new int[2];
        int start=0;
        int end=nums.length-1;
        
        while(start<end){
        	int key=target-nums[start];
        	int mid=(end+start)/2;
            //进行做差,
            if(key==nums[mid]){
            	res[0]=start;
            	res[1]=mid;
            }else if(key>nums[mid]){
            	//mid=(mid+end)/2;
            	start = mid + 1;
            }else{
            	end=mid-1;
            }
            //start++;
        }
        return res;
    }

    
    public int BinSearch(int nums[],int target){
    	int index=0;
    	int s=0;
    	int e=nums.length;
    	while(s<e){
    		index=(s+e)/2;
    		if(nums[index]==target){
    			return index;
    		}else if(nums[index]>target){
    			e=index-1;
    		}else{
    			s=index+1;
    		}
    		
    	}
    	return index+1;
    }
    
    @Test
    public void test2(){
    	 int nums[]={2,3,4,6};
    	 int result=BinSearch(nums,6);
    	 System.out.println(result);
        /* int two[]=TwoSumSorted(nums,6);
         for (int i = 0; i <2; i++) {
             System.out.println("two:"+two[i]);
         }*/
    }
    

}
