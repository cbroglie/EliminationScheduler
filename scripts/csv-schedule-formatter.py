import csv
import sys

if len(sys.argv) != 2:
	exit("Usage: %s <input file>" % sys.argv[0])
inFile = sys.argv[1]

weeks = []
for i in range(17):
	weeks.append([])

reader = csv.reader(open(inFile))
for row in reader:
	# Each row is in the format team,week 1 opponent,week 2 opponent,etc
	# We want the list of games each week, so we will iterate over each
	# team's schedule, and when it is the home team, it will add the game
	# to that week's scheduled games.
	team = row.pop(0)
	for week in range(len(row)):
		opponent = row[week]
		if opponent == "BYE":
			continue
		elif opponent[0] == "@":
			continue
		weeks[week].append([team, opponent])

# Need to output the schedule in the format week,home team,away team
for week in range(len(weeks)):
	for matchup in weeks[week]:
		line = "%d,%s,%s" % (week+1, matchup[0], matchup[1])
		print line
