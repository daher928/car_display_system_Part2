<html>
<body>
<h1>Infrastructure for The BMW Test Car Display Unit</h1>
<h2>Part II</h2>

<h3>Supervisor: Boaz Mizrachi</h3>

# BMW_Display_System_Part2 [Ongoing]

Part2 of BMW Display System

Part 1 reference -> https://github.com/daher928/BMW_Display_System_Part1/

*Part 2's incentive is testing our BMW display system application in a real environment: A real vehicle with real-time data while driving.*

### Major features and modification of this part over part I:

*  Replacing TCP connection (Between PC & Android device) with USB connection, enabling data receiving from vehicle's sensors.
  * For ease of development, we divide this part into 2 phases:
          (A) The emulation phase: using Arduino as the source and data transmitter.
          (B) When *A* works, we plug in a real moving vehicle.
          
* Cloud storage for test results and possible aggregations (Using Firebase Database and Functions)

* Log-in and User registeration ability. (Using Firebase Authentication)

* UI improvements

![Image description](https://cdn.instructables.com/F11/NFGK/IJUCPG52/F11NFGKIJUCPG52.LARGE.jpg?auto=webp&frame=1&fit=bounds)

# Part 1 recap:

<h2>Abstract</h2>

<p>In order to enhance and improve the driving experience in BMW cars in general and BMW’s Autonomous cars in particular, drivers and car testers must understand and feel not only the condition of the environment and the traffic around, but also the condition of the road, for example: fraction between the vehicle's tires and the road, humidity of the road, temperature and angle.</p>

<p>Car testing is very essential, and the more the testing procedure is efficient the more the results are reliable, and time is saved. both advantages are highly desired in the era of new technology, and especially in the emerging field of Artificial Intelligence and autonomous cars, enabling a transformation and technological rise in the mobility industry. In addition, when AI and autonomous cars are mentioned, the term “Safety” pops up as a key goal. Car testing – and especially when intended to be driven autonomously – is a necessary process for assuring the safety of the passengers and the vehicle’s surrounding environment and pedestrians
</p>

<p>The BMW Test Car Display Unit project aims is to provide car drivers and testers with a friendly and easy-to-use dashboard application with the ability to graphically display the real-time data received by the car’s sensor, enabling full control over the configurations of the graphically displayed content (e.g. Resolution, Offset, Color...).</p>

Part 1 reference -> https://github.com/daher928/BMW_Display_System_Part1/

</body>


</html>
