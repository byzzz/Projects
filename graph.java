package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		ArrayList<String> shortPath = new ArrayList<>();
		boolean[] visited = new boolean[g.members.length];
		Queue<Person> queue = new Queue<>();
		Stack<String> path = new Stack<>();
		int[] prev = new int[g.members.length];
		int[] dist = new int[g.members.length];
		for(int i=0; i<prev.length;i++) {
			prev[i]=-1;
			visited[i]=false;
			dist[i]=Integer.MAX_VALUE;
		}
		visited[g.map.get(p1)]=true;
		queue.enqueue(g.members[g.map.get(p1)]);
		dist[g.map.get(p1)]=0;
		while(!queue.isEmpty()) {
			Person currPerson = queue.dequeue();
			for(Friend f=currPerson.first;f!=null;f=f.next) {
				int vnum = f.fnum;
				if(!visited[vnum]) {
					visited[vnum]=true;
					prev[vnum]= g.map.get(currPerson.name);
					dist[vnum]= dist[g.map.get(currPerson.name)]+1;
					queue.enqueue(g.members[vnum]);
				}
			}
		}
		Person currNode = g.members[g.map.get(p2)];
		while(prev[g.map.get(currNode.name)]!=-1) {
			path.push(currNode.name);
			currNode = g.members[prev[g.map.get(currNode.name)]];
		}
		if(!path.isEmpty()) {
			shortPath.add(p1);
			while(!path.isEmpty()) {
				shortPath.add(path.pop());
			}
		}
		return shortPath;
	}
	
	Node(float coeff, int degree, Node next)
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		boolean[] visited = new boolean[g.members.length];
		Queue<Person> bfsq = new Queue<>();
		for(int v=0; v<visited.length; v++) {
			if(!visited[v] && g.members[v].school!=null && g.members[v].school.equals(school)) {
				result.add(bfsAdd(g,g.members[v], visited,v, bfsq,school));
			}
		}
		return result;
	}
	private static ArrayList<String> bfsAdd(Graph g,Person n, boolean[] visit,int v, Queue<Person> queue,String school) {
		ArrayList<String> clique = new ArrayList<>();
		visit[v]=true;
		queue.enqueue(n);
		clique.add(n.name);
		while(!queue.isEmpty()) {
			Person currPerson = queue.dequeue();
			for(Friend f=currPerson.first;f!=null;f=f.next) {
				int vnum = f.fnum;
				if(!visit[vnum]) {
					visit[vnum]=true;
					if(g.members[vnum].school!=null && g.members[vnum].school.equals(school)) {
					clique.add(g.members[vnum].name);
					queue.enqueue(g.members[vnum]);
					}
				}
			}
		}
		return clique;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		ArrayList<String> connectors = new ArrayList<>();
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		int dfsCount=0, backCount=0;
		for (int v=0; v < visited.length; v++) {
			visited[v] = false;
		}
		for (int v=0; v < visited.length; v++) {
			if (!visited[v]) {
				Person start = g.members[v];
				dfsnum[v]=1;
				back[v]=dfsnum[v];
				dfs(start,v, visited, g,dfsnum, back, dfsCount, backCount,connectors);
			}
		}
		return connectors;
	}
	
	private static void dfs(Person start, int v, boolean[] visited, Graph g, int[] dfsnum, int[] back,int dfsCount, int backCount,ArrayList<String> connectors) {
		visited[v] = true;
		dfsnum[v]=dfsCount;
		dfsCount++;
		back[v]=backCount;
		backCount++;
		for (Friend f=g.members[v].first;f!=null;f=f.next) {
			if (!visited[f.fnum]) {
				dfs(start,f.fnum, visited,g, dfsnum, back,dfsCount,backCount, connectors);
				if (dfsnum[v]>back[f.fnum]) {
					back[v]=Math.min(back[v], back[f.fnum]);
				} else if (dfsnum[v]<=back[f.fnum]){
					if(g.members[v].name!=start.name && !connectors.contains(g.members[v].name) && g.members[v].first.next!=null) {
						connectors.add(g.members[v].name);
					} else if(g.members[v].name==start.name && g.members[v].first.next==null) {
						//nothing
					} else if(!connectors.contains(g.members[v].name)){
						connectors.add(g.members[v].name);
					}
				}
			} else {
				back[v]=Math.min(back[v],dfsnum[f.fnum]);
			}
		}
	}
}


