package net.geoprism.registry;

import org.wololo.jts2geojson.GeoJSONReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.RunwayException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.Session;
import com.vividsolutions.jts.geom.Geometry;

import net.geoprism.registry.io.InvalidGeometryException;

public class GeometryTester
{
  public static void main(String[] args)
  {
    String geometryType = "MultiPolygon";
    
    String coords = "[[[[27.82067400040495, -15.270402000152046], [27.82081907454085, -15.270797190138069], [27.820791000404483, -15.27070899992026], [27.82067400040495, -15.270402000152046], [27.820589000082464, -15.270178999760844], [27.820870999597105, -15.269409000225039], [27.8215330004478, -15.268926999581197], [27.822449999567993, -15.268298000252344], [27.823183000095526, -15.2672179999106], [27.823691000139718, -15.267131500418259], [27.824199000183853, -15.267045000026599], [27.824865000319676, -15.267312999794967], [27.825558999948043, -15.267686999954947], [27.826649999897768, -15.268203000368032], [27.827615000332173, -15.26755700025518], [27.828573000443782, -15.267892000415316], [27.829411999564286, -15.267963000092209], [27.830138999815176, -15.268155999999237], [27.830815999558922, -15.268379000390382], [27.831457000340436, -15.26809199974582], [27.83195800006183, -15.268321000413664], [27.833334999710644, -15.267709000070113], [27.833434999825556, -15.26681800035027], [27.833788999962565, -15.266565000374328], [27.83422200014536, -15.266144999891651], [27.834945000211746, -15.265896000100156], [27.835083000280406, -15.266533999844057], [27.835489000117434, -15.266465000259416], [27.836457999836966, -15.26599500016897], [27.836660000159043, -15.265162999572624], [27.83664650043579, -15.264943500242168], [27.836632999813276, -15.26472400001245], [27.836924999789062, -15.263983000015287], [27.837407999579682, -15.26348500043224], [27.83743399987935, -15.26237200036735], [27.837984000061738, -15.262003000437971], [27.837829000108457, -15.261487000024886], [27.837301000041293, -15.261399999610148], [27.836740000250984, -15.260905000165451], [27.83622099969955, -15.260854999658363], [27.83626700002219, -15.26036100025982], [27.835802000162346, -15.260127000260695], [27.835986999700424, -15.259675000101026], [27.8364019999525, -15.259160999780136], [27.836731999882034, -15.258909999896446], [27.837165000064886, -15.25825600031402], [27.837317999925915, -15.257746000177633], [27.837753000200962, -15.257546999993906], [27.838114999807544, -15.256604999720821], [27.838417000244533, -15.255949000046144], [27.83905900017288, -15.255330000279116], [27.83994200042315, -15.255501000070922], [27.84045699989082, -15.255891000069425], [27.84119599979573, -15.255786999770066], [27.84204999960798, -15.255556999955445], [27.842477000413396, -15.254458999682925], [27.842632000366677, -15.253343000379004], [27.842937000042696, -15.252909000150055], [27.842668000228173, -15.25228600019858], [27.843250000087608, -15.252255999714464], [27.844082999830732, -15.25325499991817], [27.844708999920556, -15.253307999664287], [27.84564199977865, -15.252821999735318], [27.845630000124572, -15.252060999715184], [27.84595599986966, -15.251613999786116], [27.84672300016649, -15.251994000222794], [27.847112000118898, -15.251752999900873], [27.84713500028022, -15.25123600034101], [27.84730500002587, -15.250622999951304], [27.84768400041645, -15.250087000414567], [27.849640999832445, -15.244976000341978], [27.851461000125198, -15.241234999595406], [27.851607999709586, -15.239326999741081], [27.853530000209503, -15.23603399986905], [27.854211000137695, -15.234791000104394], [27.85497400025008, -15.234839999666065], [27.855737000362467, -15.234889000127112], [27.855953000430816, -15.234956999665656], [27.857118000195726, -15.236131999891711], [27.857763000262423, -15.237373999610213], [27.859891000369544, -15.237823999677687], [27.861991000084743, -15.237907999954075], [27.863099999965186, -15.239474000224789], [27.8648390001199, -15.239939000084632], [27.865955000323197, -15.240818000150455], [27.867029000388243, -15.24182699991593], [27.8690490000115, -15.241964999984589], [27.870659999659438, -15.242836999727615], [27.87254300015951, -15.243923000346001], [27.87467100026663, -15.245720999624268], [27.87647199968319, -15.246039999945879], [27.87693200021181, -15.247897000146281], [27.87767600034732, -15.250207999699285], [27.877951999585264, -15.250952999880894], [27.878758999928095, -15.251725999555049], [27.879670999717007, -15.25176499955495], [27.880672000012964, -15.251932000061572], [27.881228999618827, -15.25271700028918], [27.882118000145738, -15.253017999780695], [27.882442999844727, -15.25329600001021], [27.882768000443036, -15.25357400023978], [27.883852000069908, -15.254033999869023], [27.88477500036612, -15.254598999843836], [27.885266999672467, -15.255542000163018], [27.885627000086174, -15.255962999792473], [27.88687700017357, -15.256174999676318], [27.88748700042487, -15.256569999905423], [27.887985000007916, -15.257130999695732], [27.888718999682283, -15.257414000155848], [27.889768000393076, -15.257270999856587], [27.890735000020413, -15.257407999879149], [27.89169600027037, -15.257350999948528], [27.892273999945303, -15.256627999882141], [27.892673000358855, -15.256187000229716], [27.89316299957295, -15.256100999861076], [27.893923999593085, -15.255763999608746], [27.894555999959607, -15.255682000323873], [27.895210000441352, -15.255909000000202], [27.896174999976438, -15.256087000114803], [27.8967030000436, -15.25648799972123], [27.89735899971822, -15.256667999928084], [27.898168000153248, -15.256977999834646], [27.89838600031385, -15.257062000111034], [27.898361000060277, -15.257615999578547], [27.898858999643267, -15.25816999994538], [27.8992289996188, -15.258596999851477], [27.900043000284427, -15.259012000103553], [27.90047999975235, -15.25958500044726], [27.900732999728348, -15.260022999961336], [27.901122999726795, -15.260623999797588], [27.90154600034782, -15.261217000164265], [27.902032000276733, -15.261215000072013], [27.902658000366614, -15.261274000094886], [27.90312000008811, -15.261581999909197], [27.903679999832264, -15.261776999908477], [27.9042320001069, -15.262701000250786], [27.9046529997363, -15.263095999580571], [27.90543499982556, -15.263107000087814], [27.90601899977719, -15.263313999741115], [27.90684099991239, -15.263505999602046], [27.90748199979464, -15.263501000270765], [27.908375999652833, -15.263641999578454], [27.90957600013246, -15.264415000151985], [27.91056599992112, -15.265089999803479], [27.91118399964205, -15.265062000310934], [27.91225999979929, -15.265126999711129], [27.91359900039373, -15.265137000172274], [27.91488799958171, -15.265198000287342], [27.91613300033788, -15.265747000423573], [27.91714200010341, -15.266514999867184], [27.918345999868166, -15.267202000072075], [27.91971900023185, -15.267498000232365], [27.92073400027408, -15.268174999976111], [27.922334000314038, -15.268965000434264], [27.923517000009724, -15.269844999646864], [27.92477000023547, -15.270737000312181], [27.925909999746864, -15.271799999869927], [27.92750699964847, -15.272165999661013], [27.928628000082313, -15.273074000164797], [27.929700000055107, -15.273784999677844], [27.930825999820172, -15.274314999837202], [27.932244999607235, -15.274131000345221], [27.933172000087893, -15.274392999836891], [27.933592999717348, -15.274776000411919], [27.934972000357675, -15.274957999811704], [27.93590500021577, -15.275574000339702], [27.936596999751885, -15.276292000175488], [27.93801300029986, -15.276476999713623], [27.939206999603527, -15.277046999918923], [27.940262999737797, -15.27704399978063], [27.941307000218046, -15.277052000149524], [27.942478000259655, -15.277367000286688], [27.943268999864642, -15.27717100024131], [27.943904000369514, -15.277577000078338], [27.944620000113105, -15.278017999730764], [27.945433283119712, -15.278625975608122], [27.945848000085277, -15.278935999796374], [27.94697099971205, -15.279822000184993], [27.948027999892417, -15.280842999604602], [27.949014000395948, -15.281610999947532], [27.95013199979212, -15.281820999739182], [27.95111800029565, -15.282585999943763], [27.951657025151746, -15.282927707247723], [27.951777457863557, -15.283004053394166], [27.953826000169954, -15.284091000099409], [27.954673602205162, -15.284793257302795], [27.956395583589483, -15.285860400936599], [27.973954941867248, -15.294300533842431], [27.97544518684606, -15.294493140745715], [27.980934357234958, -15.286795381101797], [27.985994058179415, -15.279699889338247], [27.98827102738619, -15.27650677266928], [27.99203756348294, -15.271224754818434], [27.99333232462726, -15.26940904159386], [28.000254999968433, -15.259701000400696], [28.000398000267637, -15.259677000193221], [28.001348000010353, -15.259886999984928], [28.0016876640546, -15.260833421325174], [28.001802000262273, -15.261151999864751], [28.004707000228052, -15.262069999930418], [28.007058999873095, -15.264397000221209], [28.008295000214275, -15.266736000166134], [28.00865100044348, -15.269533999694147], [28.01210499964617, -15.26985199996966], [28.014950999589132, -15.26990399966968], [28.01534034038457, -15.269829466556473], [28.018429999944715, -15.269238000433234], [28.021032000326727, -15.268589000182033], [28.022892999812257, -15.269543000109195], [28.02453199985206, -15.268766000250537], [28.026070999777005, -15.26955599980937], [28.02895699976591, -15.270954999573405], [28.03113100019567, -15.271350999848607], [28.032478000259687, -15.272745000281361], [28.033647000209044, -15.27247799965977], [28.036088000361076, -15.273350000302116], [28.037732999778257, -15.27406499999961], [28.0397389996553, -15.274127000160775], [28.043321000264143, -15.27421199958394], [28.048471000336576, -15.274442000297938], [28.05112400037251, -15.273185999933787], [28.055710999663063, -15.27406199986126], [28.05788600013898, -15.276329000083138], [28.0593754995798, -15.276968499896213], [28.06086499992, -15.277607999709232], [28.064592000020923, -15.27696099955034], [28.065897133046406, -15.27752718483157], [28.06764399957109, -15.278285000352298], [28.07014599983819, -15.278244000260258], [28.070774000020265, -15.278236999937462], [28.0709375354391, -15.278471470281943], [28.07110107085788, -15.27870594062648], [28.07242699980634, -15.280607000412601], [28.074440000006177, -15.282320000267646], [28.07740199990252, -15.281050000157222], [28.07983499968566, -15.281413999856056], [28.082424000367496, -15.279669000323963], [28.084164999715142, -15.277544000355192], [28.087189999818804, -15.277646999709134], [28.089090000203498, -15.278867000211733], [28.08944300029441, -15.280562000136058], [28.089608999855614, -15.280612999789923], [28.089812000223787, -15.280682000273941], [28.089913500407874, -15.280716500066262], [28.09001499969264, -15.280750999858583], [28.09199099998483, -15.281416999994406], [28.094543455815995, -15.282385431342448], [28.095730999785985, -15.282835999781412], [28.097637000447378, -15.283513999571255], [28.09867199961326, -15.283882000353856], [28.100899999835292, -15.28472899984331], [28.102863000427305, -15.283185999733917], [28.10775100010875, -15.28553099995554], [28.110582000259285, -15.286070999676724], [28.113572999694327, -15.28839299973697], [28.118125000068858, -15.287322999856372], [28.121607999709568, -15.2870960001801], [28.12192900012343, -15.287075000111031], [28.12397700013861, -15.289752000354383], [28.12697900008095, -15.291789999908417], [28.12947600011745, -15.292836999627696], [28.13019700009164, -15.293140000110782], [28.1311980003876, -15.292594000112842], [28.133606999963263, -15.29281300031954], [28.135372000417647, -15.293369999925403], [28.13613300043778, -15.293609000155072], [28.136905000065838, -15.293855999854372], [28.139527999617655, -15.29510499989567], [28.143089000157374, -15.295398999963709], [28.14323399964951, -15.295660000308601], [28.14391499957776, -15.296875999727433], [28.145054999988417, -15.298171000091429], [28.14506899973469, -15.300277000083327], [28.1458539999623, -15.301548000239848], [28.14767400025505, -15.301920000307632], [28.14964999964792, -15.300537000382121], [28.152325999845175, -15.301212000033615], [28.154909000250314, -15.30204499977674], [28.15604000024598, -15.303343000279085], [28.157196999641997, -15.3049030002731], [28.158900999981313, -15.304689000296946], [28.1600950001843, -15.307252999825948], [28.163302999733787, -15.307785000077558], [28.165397000071664, -15.308343999775616], [28.16832300010651, -15.30719499984923], [28.170926999681456, -15.306935999596533], [28.17272099967454, -15.307055999734416], [28.17344799992543, -15.308315000236917], [28.174551000428494, -15.309308000163924], [28.175876000377286, -15.310089000207029], [28.177172999934157, -15.311467999948093], [28.180638999690245, -15.311647000108792], [28.18265099984393, -15.313609999801486], [28.182893000211948, -15.313840999662261], [28.184125656873732, -15.315976965964069], [28.18456600002105, -15.316740000250661], [28.1855695418011, -15.317688487333669], [28.1862430000146, -15.318324999598872], [28.186658000266675, -15.320157000445079], [28.188215238442638, -15.320945481948286], [28.189341999933504, -15.321516000163115], [28.190005166305525, -15.321946594659437], [28.191457004837048, -15.321621550893724], [28.19299853995085, -15.322121417469305], [28.19414385355617, -15.322492803300065], [28.194540075963914, -15.322621284044885], [28.196162999722844, -15.321672000162494], [28.198737999759032, -15.319822000284944], [28.20081500021223, -15.32013700042205], [28.202906999558536, -15.320466000305487], [28.204574316551657, -15.321684077158295], [28.205311999849073, -15.322223000390977], [28.208772000227782, -15.324006999922972], [28.20956649999414, -15.324003999784622], [28.210360999760496, -15.324000999646273], [28.21128600014896, -15.323990000038293], [28.211736000216376, -15.324206000106642], [28.21373099958612, -15.325138999964736], [28.215960322710885, -15.327650757876029], [28.215960322710885, -15.32788265076357], [28.218665736134994, -15.328655626156035], [28.223071695602414, -15.329969684053538], [28.223844670994936, -15.331361039580145], [28.224301113805495, -15.331443138689735], [28.22456097830667, -15.332179847122063], [28.224647009252237, -15.332227736021025], [28.224733040197805, -15.332275624919987], [28.22474407487931, -15.332485290163845], [28.22495374102249, -15.332628746419346], [28.22518547652868, -15.332496325744671], [28.225284792259515, -15.332595641475507], [28.225411695593436, -15.33267288604469], [28.225538598927358, -15.332750131513194], [28.225924825370555, -15.332794272037859], [28.226223123298666, -15.332785985684552], [28.22632208739458, -15.332783237356352], [28.22650416503535, -15.332805306719365], [28.226686243575443, -15.332827376981697], [28.226730384100108, -15.333103253012553], [28.226995225449457, -15.333180498481056], [28.227094540280973, -15.333296367133471], [28.22719385601181, -15.333412234886566], [28.227436627098825, -15.333566725823573], [28.227620497987743, -15.333634826086211], [28.227734573392013, -15.333677076235915], [28.22812079983521, -15.33354465556124], [28.22838564118456, -15.333522585298908], [28.228341501559214, -15.333754321704419], [28.228396676765385, -15.334030197735274], [28.228644965193155, -15.333991574551362], [28.228893253620924, -15.33395295226677], [28.22911947268568, -15.333842601854428], [28.22934569085112, -15.333732251442086], [28.229500181788126, -15.333776391067431], [28.229654671825813, -15.333820531592096], [28.230107109056007, -15.333688110917421], [28.23023952973068, -15.333853636535935], [28.230581616548534, -15.33421779361612], [28.23087956284172, -15.334493669646974], [28.23114440419107, -15.334548844853146], [28.23164098104661, -15.333765356385925], [28.23190582239596, -15.333842601854428], [28.232262518700452, -15.334102893533554], [28.23231411910149, -15.334140548147616], [28.232461714037015, -15.334280943110855], [28.232766556331683, -15.334570915115478], [28.2330093274187, -15.334813686202494], [28.23329623903038, -15.334758510996323], [28.233583149742742, -15.334703335790152], [28.233693501054404, -15.334797133280915], [28.233803851466746, -15.334890931670998], [28.23402455229143, -15.335078527551843], [28.234089104728582, -15.33510926727871], [28.23448802420313, -15.335299228376527], [28.234852181283316, -15.335210948226518], [28.23489632090866, -15.33564131519438], [28.235437038288865, -15.335663385456712], [28.235878440837553, -15.335828911075225], [28.236308807805415, -15.336082716843748], [28.236739174773277, -15.33633652351159], [28.237037121066464, -15.336446873923933], [28.237599908709, -15.33633652351159], [28.237875784739856, -15.336347558193097], [28.237842679796017, -15.33674482021712], [28.238140626089205, -15.337120011978811], [28.238515817850896, -15.33712001197\n" + 
        "11], [28.238867257617983, -15.337580230143374], [28.238979289762597, -15.337726939246693], [28.239255165793452, -15.337914535127538], [28.239508972461294, -15.338013850858374], [28.239575182348972, -15.338333867413894], [28.239597252611304, -15.338675954231746], [28.23987312864216, -15.338819409587927], [28.239939339429156, -15.339040110412611], [28.240126935310002, -15.33924977655579], [28.240402811340857, -15.339271846818122], [28.240733862577883, -15.33907321535645], [28.2409766336649, -15.339200118690371], [28.241219404751916, -15.339327022024293], [28.241307684901926, -15.339658073261319], [28.2414401055766, -15.339790493935993], [28.241739233578926, -15.339943706037047], [28.241892542806795, -15.340022230341503], [28.24220152468081, -15.340000160079171], [28.242300839512325, -15.340231895585362], [28.242499470973996, -15.34044156172854], [28.242962942885697, -15.340507772515537], [28.243205713972714, -15.340375351840862], [28.243338134647388, -15.340540877459375], [28.243470555322062, -15.340706403077888], [28.243602975996737, -15.340882964277228], [28.243856781765203, -15.340882964277228], [28.24444163967007, -15.340982279108744], [28.244772690907098, -15.341169874989589], [28.24493821652561, -15.341500926226615], [28.24496865408031, -15.34149716885912], [28.245832056304494, -15.341390575814273], [28.245975511660674, -15.342317520536994], [28.246185177803852, -15.342637537092514], [28.246438983572375, -15.342460975893175], [28.246736929865563, -15.342869272598705], [28.247465243126612, -15.342670642036353], [28.247630768745125, -15.343189289154225], [28.247862505150636, -15.343332745409725], [28.248160451443823, -15.343078938741883], [28.248303906800004, -15.343409989978909], [28.248712204404853, -15.343509305709745], [28.24893093301631, -15.343761685153481], [28.248999115117215, -15.343840356946771], [28.249583973022084, -15.343752076796761], [28.249981235046107, -15.343994847883778], [28.250577127632482, -15.343674831328258], [28.250985424338012, -15.343542410653583], [28.251261300368867, -15.34387346278993], [28.25154821198055, -15.343730006534429], [28.252232384716933, -15.343707936272097], [28.25269151290314, -15.343811006672468], [28.252773102996457, -15.343829322265265], [28.252839312884134, -15.343387920615896], [28.25297173355881, -15.343487235447412], [28.25336347734276, -15.343514823050498], [28.25375522202603, -15.343542410653583], [28.254649060905592, -15.343829322265265], [28.255460252089108, -15.343487578988402], [28.256099911981494, -15.34321809983527], [28.25726807016548, -15.342725971925915], [28.26032480104891, -15.342245628732599], [28.263250528589197, -15.34167794967749], [28.26376768912769, -15.341548659542866], [28.26394921008813, -15.341503279752374], [28.264080480529913, -15.34145762027282], [28.264184515003535, -15.341454582362928], [28.264719569352792, -15.341438957541698], [28.264913641251724, -15.341433290913471], [28.26524270948363, -15.341423681657432], [28.26608137315702, -15.341644382482116], [28.266371192276893, -15.341705244101547], [28.266661010497387, -15.341766106620355], [28.266922944338603, -15.341821111854642], [28.267184878179762, -15.341876117988306], [28.267333851776016, -15.341876117988306], [28.26748282447295, -15.341876117988306], [28.269965710549286, -15.341500926226615], [28.27030779736714, -15.341495408885862], [28.27064988418499, -15.341489891545109], [28.271367161865214, -15.341489891545109], [28.27164303879539, -15.341522996488948], [28.271918914826244, -15.341556102332106], [28.272349281794106, -15.341627829560537], [28.27277964876197, -15.341699557688287], [28.273728662847702, -15.341843013044468], [28.273855566181624, -15.341831978362961], [28.273982469515545, -15.341820942782135], [28.274160369805372, -15.34184625150317], [28.276601298910975, -15.341411337563045], [28.276600187348947, -15.341398121126247], [28.276296759687853, -15.337790226337688], [28.276105415432767, -15.33552223416359], [28.276072261925492, -15.3351292726976], [28.27599886825334, -15.334259344093311], [28.275842000408147, -15.332400000259668], [28.275892293194943, -15.332092915955172], [28.275859071339255, -15.331848866931523], [28.275805999647332, -15.33145900003268], [28.275870459454325, -15.330935725003542], [28.27582679197303, -15.33008420732017], [28.27572599955539, -15.329505999901812], [28.27578312449174, -15.328948850109214], [28.275717623269884, -15.328381171953367], [28.275715502668504, -15.32830059809396], [28.27571021825213, -15.328099810058006], [28.27570757694326, -15.32799941514071], [28.275706255839225, -15.32794921858141], [28.27570493473513, -15.32789902112279], [28.27569578862989, -15.327551488010613], [28.27553936235188, -15.325478838477466], [28.2754337837423, -15.324079915155778], [28.275347482100756, -15.322459029260642], [28.275149697800487, -15.318897086231402], [28.276104676190016, -15.318610592804475], [28.277346147107153, -15.3182286012688], [28.278874111451273, -15.317703363918952], [28.279924587050232, -15.317345246685647], [28.28226428307039, -15.316581264513559], [28.28288501852893, -15.31639026919538], [28.285200840246773, -15.315674035628092], [28.287636033476304, -15.31491005345606], [28.28909237491331, -15.31445643901327], [28.289951855194147, -15.314169945586343], [28.292005056888, -15.31350146152306], [28.293246528704515, -15.313095595685013], [28.29415375758998, -15.312713604149337], [28.295251981793683, -15.311949621977305], [28.29685156994418, -15.310851397773604], [28.298272178618333, -15.309460767999894], [28.299982342910937, -15.307950728942842], [28.303566411061183, -15.305185355919264], [28.304785358457707, -15.30420292122767], [28.30365029892232, -15.304245344047217], [28.302222432719248, -15.304467148340052], [28.28356313613, -15.305964328890525], [28.283507684831932, -15.305964328890525], [28.286145583452083, -15.294265122137517], [28.28727551954978, -15.289672448124804], [28.287631316532213, -15.288226296905918], [28.288357579338992, -15.28527436851715], [28.288787046085474, -15.283528783526378], [28.28965429560941, -15.280003811626443], [28.29015725405418, -15.2779595168181], [28.292254575588345, -15.269434860230433], [28.2927196860648, -15.267544400753309], [28.294984000011766, -15.258340999737186], [28.308673697833058, -15.25947802788852], [28.31403956217622, -15.259923701317575], [28.314296000260356, -15.259944999961647], [28.314550065932963, -15.258809726387597], [28.31480413160557, -15.257674453712923], [28.315386671957526, -15.255071414613951], [28.31649700013662, -15.250109999676567], [28.31849399959856, -15.243926999631185], [28.331882000054406, -15.24623199980681], [28.33211700009963, -15.24627299989885], [28.332748314283435, -15.243040912008325], [28.333113677354504, -15.241170388702301], [28.33338709463885, -15.239770595736218], [28.334858999743233, -15.23223500004508], [28.312415999898633, -15.227806999992879], [28.31543100044047, -15.213659999609149], [28.31545399970247, -15.213557000255207], [28.336668744167355, -15.21776952004717], [28.33776399970901, -15.217987000399603], [28.34002799979254, -15.207023000211109], [28.340750999858926, -15.203516000362981], [28.332833499866297, -15.201991000184364], [28.324915999873667, -15.200466000005747], [28.326658893193553, -15.192302466185708], [28.327952999631407, -15.186240999622271], [28.335965234208345, -15.187844056457891], [28.343716999939772, -15.189395000278864], [28.343741326601105, -15.189239613617133], [28.343765653262437, -15.189084226955345], [28.34381430568584, -15.188773453631825], [28.344630999820936, -15.183569999655617], [28.345962000046427, -15.175089999803504], [28.332210945079225, -15.171870748835829], [28.33037099962212, -15.171439999656116], [28.33284100021217, -15.16229399966818], [28.33352800041706, -15.159749000116108], [28.3484301498317, -15.163441033676577], [28.354006269373997, -15.164822526338355], [28.356211999684206, -15.165369000278986], [28.357141000257116, -15.164837000027376], [28.358069999930706, -15.164304999775766], [28.360093999738467, -15.16298099987307], [28.36102200026528, -15.16226199999113], [28.361797000031686, -15.161416999694609], [28.362228000122286, -15.16048199974432], [28.36266999982081, -15.159536000186108], [28.363879999862263, -15.154170000045326], [28.341930000269485, -15.148936999742602], [28.343127397211788, -15.144138041936912], [28.34538000018705, -15.135109999726524], [28.365181976267365, -15.139526517234799], [28.36713899996505, -15.13996299959257], [28.367189026552524, -15.139766953681828], [28.36723905224062, -15.13957090777103], [28.370823999927836, -15.125522000040121], [28.371352803089565, -15.125648675845582], [28.385563999778924, -15.129053000095723], [28.388153369182817, -15.12814263437491], [28.394935068598727, -15.125758337377192], [28.398404924647707, -15.124538411518301], [28.402850794228925, -15.122975340339508], [28.412493999698484, -15.119584999647884], [28.41293535907971, -15.11968232967564], [28.416352999591425, -15.120436000221048], [28.424089000207346, -15.122150000122247], [28.42755261945797, -15.122354025717527], [28.43645415853274, -15.122878375436528], [28.4472635544696, -15.123515108035633], [28.45366268768305, -15.123892051676705], [28.46931400024107, -15.124813999766104], [28.477800000369882, -15.130886000088651], [28.480381999829604, -15.132595999805403], [28.493820113207846, -15.133626967310533], [28.50098692210895, -15.134176802017635], [28.507946365332657, -15.135488403866589], [28.51133378682499, -15.136126809204711], [28.51273012279711, -15.137628718390488], [28.5170340002191, -15.142258000206368], [28.520879000365767, -15.146783000235132], [28.522873451148996, -15.1491288970808], [28.528225129586247, -15.155423602816882], [28.533402158782792, -15.161512885411184], [28.535459368755028, -15.163932600408998], [28.53590822128757, -15.164460545617544], [28.539056000434243, -15.168162999622552], [28.541085536468472, -15.170503876452585], [28.548414869950477, -15.178957561242271], [28.550027000046157, -15.18081700040409], [28.552386673606293, -15.180846222974651], [28.56189699974692, -15.180963999988421], [28.574384879558693, -15.180914404176235], [28.583206023739194, -15.18087937018663], [28.589597519010454, -15.180853985922568], [28.596262258896274, -15.180827517076068], [28.607722999616897, -15.180781999689373], [28.60946806300086, -15.180811138622971], [28.627416921254053, -15.181110847587377], [28.632627847697165, -15.179314002139336], [28.636461993026728, -15.178169506017753], [28.637943000169514, -15.177575000185925], [28.63962100020916, -15.177034999565421], [28.642061000315095, -15.176996999611674], [28.64313500038014, -15.17713899986478], [28.644941000027302, -15.177382000278953], [28.647704452998255, -15.17770295392802], [28.648342000383195, -15.177776999608682], [28.65195000039239, -15.177737999608837], [28.653811999924017, -15.177886000138642], [28.654094000338034, -15.178108999630467], [28.655640999732498, -15.179331000225318], [28.656636999797854, -15.179799000223511], [28.657387009203262, -15.17996016233144], [28.664623136049045, -15.177539566897337], [28.669496206984547, -15.17785608509007], [28.67089399985838, -15.178091999745845], [28.673897999892972, -15.178633000412503], [28.675430000394385, -15.178768000342757], [28.676729000043508, -15.178277000183243], [28.676582154243192, -15.177232845918923], [28.675602455289265, -15.173491636529661], [28.67794000013106, -15.172572999743977], [28.68022300019146, -15.171998000207395], [28.68318500008786, -15.171345999817902], [28.685377000448398, -15.171013999796116], [28.687555000163286, -15.170709000120098], [28.689346000018077, -15.17030300028307], [28.69018745878435, -15.17012957861698], [28.69141300001013, -15.169877000423071], [28.692521999890573, -15.169647999755227], [28.694994999719654, -15.169078999595968], [28.69762799973256, -15.168140000360552], [28.699804000254574, -15.1673439996257], [28.702220000153034, -15.166716000343001], [28.703515499640787, -15.166447499652236], [28.704811000027803, -15.16617899986079], [28.70613750004577, -15.166131000345217], [28.707464000063737, -15.166082999930325], [28.71050399995977, -15.166885000042555], [28.710729000443166, -15.167057999926612], [28.71271999962846, -15.168612999690026], [28.71455299962139, -15.168909999896414], [28.716293102344935, -15.16882946021127], [28.717729000393206, -15.168763000312083], [28.720087000315004, -15.169217999710781], [28.72194399961603, -15.170781999889243], [28.722609999751853, -15.17086400007338], [28.724180000207014, -15.17105599993431], [28.72513550020335, -15.17111950016465], [28.726091000199688, -15.17118300039499], [28.72732500044856, -15.171092999841903], [28.729000000349913, -15.170930999565826], [28.72932913962825, -15.170912166862877], [28.730503000413307, -15.170845000096506], [28.732478999806176, -15.170616000327982], [28.735167999703606, -15.170266000375477], [28.737317999925892, -15.170053999592255], [28.73904000019604, -15.16989799959282], [28.74117199958829, -15.169788999962236], [28.742961000250148, -15.169723999662722], [28.74508600021892, -15.169568999709384], [28.74657200039769, -15.16940000000983], [28.74802499995394, -15.169404000194277], [28.749294000018267, -15.169350000402062], [28.74996899966976, -15.169332999618064], [28.753393000186975, -15.169507999594373], [28.756173364909046, -15.169657067619426], [28.75671300040483, -15.169685999708975], [28.760223000391306, -15.169836000330974], [28.763461999571803, -15.169953000330565], [28.7636870000552, -15.169958499684867], [28.76391199963922, -15.169963999938489], [28.767459000432666, -15.170041999938178], [28.769772960553837, -15.170425429489796], [28.771436000371295, -15.170700999751205], [28.772720293406962, -15.171095898356839], [28.775845999593344, -15.172057000230211], [28.78054799969061, -15.172638000043492], [28.78520200027225, -15.172573999790075], [28.78965100039352, -15.172372000367375], [28.793998000307624, -15.172141999653377], [28.79720099962657, -15.171877000023358], [28.79963600040128, -15.171359999564174], [28.800370608017317, -15.17061386443811], [28.801038000303663, -15.169936000445887], [28.801762999562925, -15.167737999808708], [28.802260562974766, -15.166686486392052], [28.803316000133464, -15.16445600044392], [28.803416000248376, -15.164021500191893], [28.8034659998562, -15.163804250065937], [28.803516000363288, -15.163586999939923], [28.803882000154374, -15.162734000173828], [28.804396999621986, -15.160937000042338], [28.815798000177722, -15.159912000438283], [28.825369057536136, -15.159051520111404], [28.82719899983414, -15.158886999934964], [28.831835999631835, -15.159358000071506], [28.83470099955167, -15.161952000084568], [28.837767999793527, -15.164770999681707], [28.840702000197325, -15.165257999656774], [28.845059999719354, -15.165268000117976], [28.845429999694886, -15.165236999587705], [28.846143557779158, -15.165170626922759], [28.84901000021148, -15.164904000419142], [28.853883000100552, -15.164736999912463], [28.855641500255217, -15.164847999635299], [28.857400000409882, -15.164959000257511], [28.860712000258786, -15.165064999749802], [28.86395799976202, -15.165285000002598], [28.866080999638598, -15.165341999933219], [28.868883000250435, -15.165453999702152], [28.871261000195148, -15.165158000441181], [28.873240999772463, -15.164787999566386], [28.874750000112556, -15.164833999889026], [28.878369999775828, -15.158211000237316], [28.88138872710823, -15.152681046492944], [28.881868563083856, -15.151802041930523], [28.892717217126062, -15.131928579640089], [28.902519225781816, -15.113972448569484], [28.90778377070461, -15.10432842022027], [28.910136419660205, -15.100018643245619], [28.913603413167152, -15.093667517641677], [28.91959559416148, -15.082690543833394], [28.920358932418935, -15.08129219877577], [28.921769382351272, -15.078708419557756], [28.927784548218483, -15.067689340390586], [28.928120999884413, -15.067072999918878], [28.93553778074414, -15.054568310878665], [28.936034999715616, -15.053729999739517], [28.938417172514676, -15.04971212831191], [28.939238999980034, -15.048325999644987], [28.942299023281464, -15.043164838086625], [28.942443000244452, -15.042921999550458], [28.963463438986707, -15.004551285013633], [28.969050836027407, -14.994352047608743], [28.969185225318085, -14.994106735037633], [28.973353806033515, -14.993822982744291], [28.987883921206162, -14.99291276810959], [28.99031791833744, -14.992760294351967], [28.99196222257865, -14.99265728960205], [28.9999999\n" + 
        "70038, -14.992139715375401]]]]";
    
    try
    {
      getGeometry(coords, geometryType);
    }
    catch (Throwable t)
    {
      System.out.println(RunwayException.localizeThrowable(t, Session.getCurrentLocale()));
    }
  }
  
  @Request
  public static Geometry getGeometry(String input, String geometryType)
  {
    try
    {
      String sCoordinates = input;
      
      if (sCoordinates.endsWith(","))
      {
        sCoordinates = sCoordinates.substring(0, sCoordinates.length()-1);
      }
      
      // remove newlines and spaces
      sCoordinates = sCoordinates.replace("\n", "").replace("\r", "").replaceAll("\\s+","");
      
      JsonArray joCoordinates = JsonParser.parseString(sCoordinates).getAsJsonArray();
      
      // TODO : Not sure if we want to keep this polygon -> multipolygon conversion code
      if (geometryType.toUpperCase().equals("POLYGON"))
      {
        geometryType = "MultiPolygon";
        
        JsonArray joCoordinates2 = new JsonArray();
        joCoordinates2.add(joCoordinates);
        joCoordinates = joCoordinates2;
      }
      
      JsonObject joGeometry = new JsonObject();
      {
        joGeometry.add("coordinates", joCoordinates);
        
        joGeometry.addProperty("type", geometryType);
      }
      
      GeoJSONReader reader = new GeoJSONReader();
      Geometry jtsGeom = reader.read(joGeometry.toString());
      
      return jtsGeom;
    }
    catch (Throwable t)
    {
      InvalidGeometryException geomEx = new InvalidGeometryException(t);
      geomEx.setReason(RunwayException.localizeThrowable(t, Session.getCurrentLocale()));
      throw geomEx;
    }
  }
}
