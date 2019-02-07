5 poke 53280,0:poke 53281,0:poke646,15:poke650,128
10 print chr$(147);"initializing sprites...";:v = 53248
11 poke v+21,1
12 poke 2040,13:poke53287,14:poke56325,29
20 for n = 0 to 62:read q:poke832+n,q:poke896+n,q:poke 49152+n,q:next
23 print"done":print"initializing machine code...";
25 gosub 30000:print"done":gosub1000
30 x=150:y=80:poke49663,0
50 xh=x/256:xl=x and 255
52 poke49659,xl:poke49660,xh:poke49661,y
55 sys 49664
60 poke198,0:wait198,1:geta$
70 if a$="x" then printchr$(147);:pokev+21,0:poke56325,40:end
80 if a$="w" then y=y-3:goto 115
90 if a$="a" then x=x-3:goto 118
100 if a$="d" then x=x+3:goto 118
110 if a$="s" then y=y+3:goto 115
112 goto 60
115 if y<50 then y=50
116 if y>250 then y=250
117 goto 50
118 if x<24 then x=24
119 if x>320 then x=320
120 goto 50
200 data 0,127,0,1,255,192,3,255,224,3,231,224
210 data 7,217,240,7,223,240,7,217,240,3,231,224
220 data 3,255,224,3,255,224,2,255,160,1,127,64
230 data 1,62,64,0,156,128,0,156,128,0,73,0,0,73,0
240 data 0,62,0,0,62,0,0,62,0,0,28,0

1000 print chr$(147);
1020 for i=0to150:x=rnd(0)*40:y=rnd(0)*24
1025 of=x+y*40
1030 poke1024+of,126:poke55296+of,7:next
1040 for i=0to6:x=rnd(0)*22+6:y=int(rnd(0)*18)+2
1050 for p=x-2tox+2:of=p+y*40:poke1024+of,104:poke55296+of,15:next
1055 y=y+1
1060 for p=x-3tox+3:of=p+y*40:poke1024+of,104:poke55296+of,15:next
1065 y=y+1
1070 for p=x-6tox+6:of=p+y*40:poke1024+of,104:poke55296+of,15:next
1075 y=y+1
1080 for p=x-4tox+4:of=p+y*40:poke1024+of,104:poke55296+of,15:next
1100 next
1102 print chr$(19);chr$(17);chr$(17);chr$(17);chr$(17);chr$(17);
1104 print chr$(29);chr$(29);chr$(29);chr$(29);
1110 print"** use wasd to move, x to exit **"
1120 return

30000 for qq=49664 to 50535:read dq:pokeqq,dq:next
30001 return
30002 data 32,46,197,169,0,141,62,197,141,63,197,141,64,197,141,67,197,141
30003 data 68,197,141,69,197,141,71,197,141,76,197,141,75,197,141,66,197
30004 data 141,65,197,173,251,193,141,83,197,173,252,193,141,84,197,173
30005 data 253,193,141,85,197,169,208,141,72,197,173,24,208,41,6,201,6,208
30006 data 9,173,72,197,24,105,8,141,72,197,32,104,196,169,64,141,79,197
30007 data 238,103,197,173,103,197,201,15,208,5,169,13,141,103,197,141,81
30008 data 197,32,7,197,173,79,197,141,77,197,173,80,197,141,78,197,169
30009 data 0,141,62,197,141,63,197,141,66,197,141,65,197,32,84,196,174,62
30010 data 197,189,0,192,240,66,141,67,197,32,183,195,174,76,197,240,6,14
30011 data 64,197,202,208,250,169,255,77,64,197,45,67,197,141,67,197,173
30012 data 76,197,240,12,173,63,197,41,3,201,3,240,3,32,239,195,173,77,197
30013 data 24,109,62,197,133,34,173,78,197,105,0,133,35,173,67,197,145,34
30014 data 174,62,197,232,232,232,142,62,197,238,75,197,238,65,197,173,75
30015 data 197,201,8,176,14,173,65,197,201,8,176,7,224,63,176,3,76,137,194
30016 data 224,65,144,3,76,126,195,173,75,197,201,8,208,18,173,66,197,208
30017 data 13,173,65,197,201,8,176,6,32,207,195,76,137,194,173,66,197,240
30018 data 14,173,253,193,56,233,8,141,253,193,169,0,141,66,197,173,251
30019 data 193,24,105,8,141,251,193,173,252,193,105,0,141,252,193,238,63
30020 data 197,173,63,197,41,3,201,3,208,35,173,63,197,24,105,21,141,63
30021 data 197,173,251,193,56,233,24,141,251,193,173,252,193,233,0,141,252
30022 data 193,173,253,193,24,105,8,141,253,193,173,63,197,141,62,197,32
30023 data 84,196,32,104,196,169,0,141,65,197,76,137,194,32,54,197,173,255
30024 data 193,168,10,170,173,83,197,157,0,208,232,173,85,197,157,0,208
30025 data 185,95,197,174,84,197,240,9,13,16,208,141,16,208,76,173,195,73
30026 data 255,45,16,208,141,16,208,173,103,197,174,255,193,157,248,7,96
30027 data 173,73,197,24,109,75,197,133,34,173,74,197,105,0,133,35,160,0
30028 data 177,34,141,64,197,96,169,7,56,237,75,197,141,65,197,169,0,141
30029 data 75,197,173,253,193,24,105,8,141,253,193,32,104,196,169,1,141
30030 data 66,197,96,173,68,197,201,39,144,1,96,173,251,193,24,105,8,141
30031 data 251,193,173,252,193,105,0,141,252,193,32,104,196,32,183,195,169
30032 data 8,56,237,76,197,170,240,6,78,64,197,202,208,250,174,76,197,189
30033 data 86,197,141,70,197,169,255,77,64,197,141,79,197,169,255,77,70
30034 data 197,45,79,197,13,70,197,45,67,197,141,67,197,173,251,193,56,233
30035 data 8,141,251,193,173,252,193,233,0,141,252,193,32,104,196,96,173
30036 data 253,193,56,233,50,41,7,141,75,197,173,251,193,41,7,141,76,197
30037 data 96,173,251,193,56,233,24,141,79,197,173,252,193,233,0,141,80
30038 data 197,32,225,196,173,79,197,141,68,197,173,253,193,56,233,50,74
30039 data 74,74,141,69,197,141,81,197,169,40,141,79,197,32,7,197,173,79
30040 data 197,24,109,68,197,141,79,197,173,80,197,105,0,141,80,197,173
30041 data 79,197,24,105,0,133,34,173,80,197,105,4,133,35,160,0,140,80,197
30042 data 177,34,201,126,208,2,169,32,141,79,197,32,244,196,173,79,197
30043 data 24,109,71,197,141,73,197,173,80,197,109,72,197,141,74,197,96
30044 data 78,80,197,110,79,197,78,80,197,110,79,197,78,80,197,110,79,197
30045 data 96,14,79,197,46,80,197,14,79,197,46,80,197,14,79,197,46,80,197
30046 data 96,169,0,168,140,80,197,240,17,24,109,79,197,170,152,109,80,197
30047 data 168,138,14,79,197,46,80,197,78,81,197,176,234,208,243,141,79
30048 data 197,140,80,197,96,120,165,1,41,251,133,1,96,165,1,9,4,133,1,88
30049 data 96,0,0,0,0,0,0,0,0,0,0,208,0,0,0,0,0,0,0,0,0,0,0,0,0,255,254
30050 data 252,248,240,224,192,128,0,1,2,4,8,16,32,64,128,13


























