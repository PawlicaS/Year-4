import matplotlib.pyplot as plt
import numpy as np

# sample data
data = {'Key 1': [10, 20, 30, 40],
        'Key 2': [15, 25, 35, 45],
        'Key 3': [20, 30, 40, 50]}

# set the keys and values
keys = list(data.keys())
values = np.array(list(data.values()))

# set the positions of the bars on the x-axis
x_pos = np.arange(len(keys))

# set the width of each bar
bar_width = 0.2

# create a bar for each value
fig, ax = plt.subplots()
for i in range(len(values[0])):
    ax.bar(x_pos + i * bar_width, values[:,i], width=bar_width, label='Value {}'.format(i+1))

# add labels and title
ax.set_xlabel('Keys')
ax.set_ylabel('Values')
ax.set_title('Bar Graph with Four Values for Each Key')
ax.set_xticks(x_pos)
ax.set_xticklabels(keys)
ax.legend()

# display the graph
plt.show()
