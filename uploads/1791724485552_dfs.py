graph={
    'A':['B','C'],
    'B':['D','E'],
    'C':['F','G'],
    'D':[],
    'E':[],
    'F':[],
    'G':[]
}
visited=[]
def dfs(node):
    if node not in visited:
        visited.append(node)

        for neighbor in reversed (graph[node]):
            dfs(neighbor)

dfs('A')
print(visited)            