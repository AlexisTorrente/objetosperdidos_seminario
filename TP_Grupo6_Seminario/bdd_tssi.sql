

USE `southern_TP_FINAL_TSSI_G4`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Caja de objetos`
--

CREATE TABLE `Caja de objetos` (
  `ID_Caja` int(11) NOT NULL,
  `ID_Localidad` int(11) NOT NULL,
  `Descripcion` varchar(255) NOT NULL,
  `Latitud` varchar(25) CHARACTER SET utf8mb4 DEFAULT NULL,
  `Longitud` varchar(25) CHARACTER SET utf8mb4 DEFAULT NULL,
  `Estado` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Caja de objetos`
--

INSERT INTO `Caja de objetos` (`ID_Caja`, `ID_Localidad`, `Descripcion`, `Latitud`, `Longitud`, `Estado`) VALUES
(1, 1, 'Municipio de Tigre', '-34.4242136', '-58.580236', 1),
(2, 2, 'Ubicada en la municipalidad de San Isidrio', '-34.4736463', '-58.5139897', 1),
(3, 73, 'Delegación General Pacheco', '-34.45525', '-58.6350706', 1),
(4, 72, 'Delegación Municipal de Benavidez', '-34.4119897', '-58.6909701', 1),
(5, 3, 'Municipalidad de San Fernando', '-34.4407583', '-58.557399', 1),
(6, 4, 'Municipalidad de Victoria', '-32.6222596', '-60.1600566', 1),
(7, 5, 'Municipalidad de Virreyes', '-34.4407583', '-58.557399', 1),
(8, 12, 'Municipalidad de Rosario', '-32.947091', '-60.7223155', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Comentarios perfil`
--

CREATE TABLE `Comentarios perfil` (
  `ID_ComentarioPerfil` int(11) NOT NULL,
  `ID_UsuarioComentario` int(11) NOT NULL,
  `ID_UsuarioPerfil` int(11) NOT NULL,
  `Comentario` varchar(255) NOT NULL,
  `Estado` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Comentarios perfil`
--

INSERT INTO `Comentarios perfil` (`ID_ComentarioPerfil`, `ID_UsuarioComentario`, `ID_UsuarioPerfil`, `Comentario`, `Estado`) VALUES
(1, 2, 1, 'Buen perfil, bienvenido!', 1),
(2, 6, 5, '¡Gracias por recuperar mi DNI!', 1),
(3, 7, 5, '¡Gracias por ayudarme a recuperar mi sube capo!', 1),
(4, 5, 1, 'Vi tu publicacion! Voy a ver si puedo ayudarte!', 1),
(5, 5, 1, 'Creo que vi tu DNI por...', 1),
(6, 5, 1, 'Prueba de comentario 3', 1),
(7, 5, 6, 'Hola maria!', 1),
(8, 5, 7, 'Hola messi!', 1),
(9, 1, 2, 'Gracias por encontrar mi calcular!', 1),
(10, 1, 5, 'Sos un genio! Gracias por encontrar mis llaves', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Comentarios publicacion`
--

CREATE TABLE `Comentarios publicacion` (
  `ID_ComentarioPublicacion` int(11) NOT NULL,
  `ID_Usuario` int(11) NOT NULL,
  `ID_Publicacion` int(11) NOT NULL,
  `Comentario` varchar(255) NOT NULL,
  `Estado` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Comentarios publicacion`
--

INSERT INTO `Comentarios publicacion` (`ID_ComentarioPublicacion`, `ID_Usuario`, `ID_Publicacion`, `Comentario`, `Estado`) VALUES
(1, 2, 8, 'Gracias por publicar! Esperemos que el dueño pueda recuperar su DNI pronto, que ya sabemos lo complicado que esos tramites pueden llegar a ser!', 1),
(2, 4, 8, '[Accion condenable]', 0),
(3, 1, 13, 'Espero que las encuentres', 1),
(4, 1, 12, 'Probaste usando la app \"encontrar celular\"?', 1),
(5, 1, 13, 'Te recomiendo cambiar la cerradura de tu casa!!', 1),
(6, 1, 8, 'Suerte!!', 1),
(7, 1, 9, 'espero que la encuentres!', 1),
(8, 1, 12, 'Suerte!', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Generos`
--

CREATE TABLE `Generos` (
  `ID_Genero` int(11) NOT NULL,
  `Descripcion` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Generos`
--

INSERT INTO `Generos` (`ID_Genero`, `Descripcion`) VALUES
(1, 'Hombre'),
(2, 'Mujer'),
(3, 'Otro');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Localidades`
--

CREATE TABLE `Localidades` (
  `ID_Localidad` int(11) NOT NULL,
  `ID_Provincia` int(11) NOT NULL,
  `Nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Localidades`
--

INSERT INTO `Localidades` (`ID_Localidad`, `ID_Provincia`, `Nombre`) VALUES
(1, 1, 'Tigre'),
(2, 1, 'San Isidro'),
(3, 1, 'San Fernando'),
(4, 1, 'Victoria'),
(5, 1, 'Virreyes'),
(6, 2, 'Calamuchita'),
(7, 2, 'Cosquin'),
(8, 2, 'Gral Paz'),
(9, 3, 'Iguazu'),
(10, 3, '25 de Mayo'),
(11, 3, 'Apostoles'),
(12, 4, 'Rosario'),
(13, 4, 'Santa Fe'),
(14, 4, 'Rafaela'),
(15, 5, 'Piedras Chatas'),
(16, 5, 'Pantanillo'),
(17, 5, 'Jarilla'),
(18, 6, 'Godoy Cruz'),
(19, 6, 'Guaymallen'),
(20, 6, 'El Sosneado'),
(21, 7, 'Belen'),
(22, 7, 'Aconquija'),
(23, 7, 'El Alto'),
(24, 8, 'Colonia Elisa'),
(25, 8, 'Concepción del Bermejo'),
(26, 8, 'Charata'),
(27, 9, 'Corcovado'),
(28, 9, 'El Maiten'),
(29, 9, 'Bahía Bustamante'),
(30, 10, 'Bella Vista.'),
(31, 10, 'Esquina'),
(32, 10, 'Caa Catí'),
(33, 11, 'Gualeguaychu'),
(34, 11, 'Concordia'),
(35, 11, 'Colon'),
(36, 12, 'Clorinda'),
(37, 12, 'Espinillo'),
(38, 12, ' Laguna Yema'),
(39, 13, 'Volcan'),
(40, 13, 'Purmamarca'),
(41, 13, 'Tilcara'),
(42, 14, 'Hucal'),
(43, 14, 'Alpachiri'),
(44, 14, 'Guatraché'),
(45, 15, 'Chilecito'),
(46, 15, 'Famatina'),
(47, 15, 'Guandacol'),
(48, 16, 'Copahue'),
(49, 16, 'Arroyito'),
(50, 16, 'Chos Malal'),
(51, 18, 'Cachi'),
(52, 18, 'Cafayate'),
(53, 18, 'San Carlos'),
(54, 17, 'Bariloche'),
(55, 17, 'Cipolletti'),
(56, 17, 'El Bolson'),
(57, 19, 'Rivadavia'),
(58, 19, 'Santa Lucía'),
(59, 19, 'Alto de Sierra'),
(60, 20, 'Río Gallegos'),
(61, 20, 'Las Heras'),
(62, 20, 'Los Antiguos'),
(63, 21, 'Añatuya'),
(64, 21, 'Bandera'),
(65, 21, 'Frias'),
(66, 22, 'Rio Grande'),
(67, 22, 'Tolhuin'),
(68, 22, 'Ushuaia'),
(69, 23, 'Tafi Viejo'),
(70, 23, 'Raco'),
(71, 23, 'Burruyacu'),
(72, 1, 'Benavidez'),
(73, 1, 'Pacheco');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Notificaciones`
--

CREATE TABLE `Notificaciones` (
  `ID_Notificaciones` int(11) NOT NULL,
  `ID_Usuario` int(11) NOT NULL,
  `ID_Publicacion` int(11) NOT NULL,
  `ID_Localidad` int(11) NOT NULL,
  `Descripcion` varchar(255) NOT NULL,
  `Fecha_hora` varchar(255) NOT NULL,
  `Estado` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Provincias`
--

CREATE TABLE `Provincias` (
  `ID_Provincia` int(11) NOT NULL,
  `Nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Provincias`
--

INSERT INTO `Provincias` (`ID_Provincia`, `Nombre`) VALUES
(1, 'Buenos Aires'),
(2, 'Córdoba'),
(3, 'Misiones'),
(4, 'Santa Fe'),
(5, 'San Luis'),
(6, 'Mendoza'),
(7, 'Catamarca'),
(8, 'Chaco'),
(9, 'Chubut'),
(10, 'Corrientes'),
(11, 'Entre Ríos'),
(12, 'Formosa'),
(13, 'Jujuy'),
(14, 'La Pampa'),
(15, 'La Rioja'),
(16, 'Neuquén'),
(17, 'Río Negro'),
(18, 'Salta'),
(19, 'San Juan'),
(20, 'Santa Cruz'),
(21, 'Santiago del Estero'),
(22, 'Tierra del Fuego'),
(23, 'Tucumán');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Publicaciones`
--

CREATE TABLE `Publicaciones` (
  `ID_Publicacion` int(11) NOT NULL,
  `ID_TipoPublicacion` int(11) NOT NULL,
  `ID_TipoObjeto` int(11) NOT NULL,
  `ID_Usuario` int(11) NOT NULL,
  `ID_Provincia` int(11) NOT NULL,
  `ID_Localidad` int(11) NOT NULL,
  `Titulo` varchar(255) NOT NULL,
  `Ubicacion` varchar(255) NOT NULL,
  `Imagen` varchar(255) DEFAULT '"Vacio"',
  `FechaPublicacion` date NOT NULL,
  `RangoHorario` varchar(255) DEFAULT NULL,
  `Descripcion` varchar(255) NOT NULL,
  `Recuperado` int(1) NOT NULL,
  `Estado` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Publicaciones`
--

INSERT INTO `Publicaciones` (`ID_Publicacion`, `ID_TipoPublicacion`, `ID_TipoObjeto`, `ID_Usuario`, `ID_Provincia`, `ID_Localidad`, `Titulo`, `Ubicacion`, `Imagen`, `FechaPublicacion`, `RangoHorario`, `Descripcion`, `Recuperado`, `Estado`) VALUES
(8, 1, 1, 1, 1, 10, 'Perdi mi celular', 'cc', 'prueba', '2023-03-20', 'ttt', 'ff', 0, 0),
(9, 1, 1, 1, 1, 1, 'Perdi mu SUBE', 'ss', 'prueba', '2023-10-16', NULL, 'xx', 1, 1),
(10, 2, 4, 1, 1, 2, 'Perdi mi DNI', 'parada del 333', 'prueba', '2023-10-14', '12:00 - 13:00', 'DNI de Francisco Javier Ciarallo ', 0, 1),
(11, 1, 5, 1, 2, 8, 'Perdí mi caruchera', 'Estación de tren Gral Rodríguez ', 'prueba', '2023-10-20', '11:00', 'Es una cartuchera de Spiderman de tres pisos', 0, 1),
(12, 1, 1, 1, 3, 10, 'Perdí mi Galaxy S20', 'Colegio Nro 22', 'prueba', '2023-10-15', '19:00', 'Es un Samsung Galaxy S20 Violeta', 0, 1),
(13, 1, 5, 5, 3, 10, 'Perdí mis llaves', 'plaza don bosco', 'prueba', '2023-10-30', '14:00', 'tienen un llavero de starwars', 0, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Soporte`
--

CREATE TABLE `Soporte` (
  `ID_Soporte` int(11) NOT NULL,
  `ID_Usuario` int(11) NOT NULL,
  `Resumen` varchar(255) NOT NULL,
  `Detalle` varchar(255) NOT NULL,
  `Estado` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Soporte`
--

INSERT INTO `Soporte` (`ID_Soporte`, `ID_Usuario`, `Resumen`, `Detalle`, `Estado`) VALUES
(1, 4, 'Me Banearon', 'Hola, queria saber por qué motivo fui baneado de los foros, lo unico que hice fue [Imagine una accion condenable aqui], no creo que eso sea motivo de ban, sinceramente.', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Tipo Objetos`
--

CREATE TABLE `Tipo Objetos` (
  `ID_TipoObjeto` int(11) NOT NULL,
  `Descripcion` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Tipo Objetos`
--

INSERT INTO `Tipo Objetos` (`ID_TipoObjeto`, `Descripcion`) VALUES
(1, 'Celular'),
(2, 'SUBE'),
(3, 'Billetera'),
(4, 'DNI'),
(5, 'Otro');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Tipo Publicaciones`
--

CREATE TABLE `Tipo Publicaciones` (
  `ID_TipoPublicacion` int(11) NOT NULL,
  `Descripcion` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Tipo Publicaciones`
--

INSERT INTO `Tipo Publicaciones` (`ID_TipoPublicacion`, `Descripcion`) VALUES
(1, 'Objeto Perdido'),
(2, 'Objeto Encontrado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Tipo Usuarios`
--

CREATE TABLE `Tipo Usuarios` (
  `ID_TipoUsuario` int(11) NOT NULL,
  `Descripcion` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Tipo Usuarios`
--

INSERT INTO `Tipo Usuarios` (`ID_TipoUsuario`, `Descripcion`) VALUES
(1, 'Usuario'),
(2, 'Moderador'),
(3, 'Administrador');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Usuarios`
--

CREATE TABLE `Usuarios` (
  `ID_Usuario` int(11) NOT NULL,
  `ID_TipoUsuario` int(11) NOT NULL,
  `ID_Provincia` int(11) NOT NULL,
  `ID_Localidad` int(11) NOT NULL,
  `ID_Genero` int(11) NOT NULL,
  `NombreUsuario` varchar(255) NOT NULL,
  `Contrasena` varbinary(255) NOT NULL,
  `Nombre` varchar(255) NOT NULL,
  `Apellido` varchar(255) NOT NULL,
  `DNI` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `FechaNacimiento` varchar(255) NOT NULL,
  `DescripcionPerfil` varchar(400) DEFAULT NULL,
  `Estado` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Usuarios`
--

INSERT INTO `Usuarios` (`ID_Usuario`, `ID_TipoUsuario`, `ID_Provincia`, `ID_Localidad`, `ID_Genero`, `NombreUsuario`, `Contrasena`, `Nombre`, `Apellido`, `DNI`, `Email`, `FechaNacimiento`, `DescripcionPerfil`, `Estado`) VALUES
(1, 3, 1, 2, 1, 'Fran22', 0x50696d50616d, 'Francisco', 'Ciarallo', '42467234', 'franCia@gmail.com', '22/03/2002', 'Descripcion de prueba.', 1),
(2, 2, 1, 3, 2, 'SQLUCIA', 0x666661617777, 'Lucia', 'SQL', '37435537', 'lu@sql.com', '12/01/1987', NULL, 1),
(3, 3, 1, 5, 3, 'ADM_J', 0x5375706572436f6e74726173656e696141646d696e697374726174697661, 'Jorge', 'Sanchez', '23768957', 'JorSanch@gmail.com', '08/09/1976', NULL, 1),
(4, 1, 1, 2, 1, 'Elba_neado', 0x756e61436f6e74726173656e6961, 'Alejandro', 'Corona', '40467286', 'AleCor@gmail.com.ar', '30/04/2000', NULL, 0),
(5, 1, 1, 2, 1, 'Alex21', 0x61313233, 'Alexis', 'Torrente', '43858291', 'Alexis21@gmail.com', '06/01/2002', 'Hola! Esto es una prueba para la descripcion del perfil!!!!', 1),
(6, 1, 4, 12, 2, 'Maria777', 0x6a65726570726f313233, 'Maria', 'Torres', '55145236', 'maria@gmail.com', '06/06/2010', 'Maria777 pro!', 0),
(7, 1, 4, 12, 1, 'el messias', 0x6c656f6d657373693130, 'Leo', 'Messi', '39555222', 'leomessi@gmail.com', '24/06/1987', NULL, 1),
(8, 1, 1, 1, 1, 'chadmin21', 0x636861646d696e3231, 'Gaston', 'Sosa', '42903969', 'gaston.sosa@alumnos.frgp.utn.edu.ar', '01/02/2001', NULL, 1),
(9, 1, 7, 21, 3, 'usuario', 0x313233, 'usuario', 'usuario', '11111111', 'usuario@gmail.com', '10/10/2002', NULL, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Caja de objetos`
--
ALTER TABLE `Caja de objetos`
  ADD PRIMARY KEY (`ID_Caja`);

--
-- Indices de la tabla `Comentarios perfil`
--
ALTER TABLE `Comentarios perfil`
  ADD PRIMARY KEY (`ID_ComentarioPerfil`);

--
-- Indices de la tabla `Comentarios publicacion`
--
ALTER TABLE `Comentarios publicacion`
  ADD PRIMARY KEY (`ID_ComentarioPublicacion`);

--
-- Indices de la tabla `Generos`
--
ALTER TABLE `Generos`
  ADD PRIMARY KEY (`ID_Genero`);

--
-- Indices de la tabla `Localidades`
--
ALTER TABLE `Localidades`
  ADD PRIMARY KEY (`ID_Localidad`);

--
-- Indices de la tabla `Notificaciones`
--
ALTER TABLE `Notificaciones`
  ADD PRIMARY KEY (`ID_Notificaciones`);

--
-- Indices de la tabla `Provincias`
--
ALTER TABLE `Provincias`
  ADD PRIMARY KEY (`ID_Provincia`);

--
-- Indices de la tabla `Publicaciones`
--
ALTER TABLE `Publicaciones`
  ADD PRIMARY KEY (`ID_Publicacion`);

--
-- Indices de la tabla `Soporte`
--
ALTER TABLE `Soporte`
  ADD PRIMARY KEY (`ID_Soporte`);

--
-- Indices de la tabla `Tipo Objetos`
--
ALTER TABLE `Tipo Objetos`
  ADD PRIMARY KEY (`ID_TipoObjeto`);

--
-- Indices de la tabla `Tipo Publicaciones`
--
ALTER TABLE `Tipo Publicaciones`
  ADD PRIMARY KEY (`ID_TipoPublicacion`);

--
-- Indices de la tabla `Tipo Usuarios`
--
ALTER TABLE `Tipo Usuarios`
  ADD PRIMARY KEY (`ID_TipoUsuario`);

--
-- Indices de la tabla `Usuarios`
--
ALTER TABLE `Usuarios`
  ADD PRIMARY KEY (`ID_Usuario`),
  ADD UNIQUE KEY `NombreUsuario_UNIQUE` (`NombreUsuario`),
  ADD UNIQUE KEY `Email_UNIQUE` (`Email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Caja de objetos`
--
ALTER TABLE `Caja de objetos`
  MODIFY `ID_Caja` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT de la tabla `Comentarios perfil`
--
ALTER TABLE `Comentarios perfil`
  MODIFY `ID_ComentarioPerfil` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT de la tabla `Comentarios publicacion`
--
ALTER TABLE `Comentarios publicacion`
  MODIFY `ID_ComentarioPublicacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT de la tabla `Generos`
--
ALTER TABLE `Generos`
  MODIFY `ID_Genero` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT de la tabla `Localidades`
--
ALTER TABLE `Localidades`
  MODIFY `ID_Localidad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;
--
-- AUTO_INCREMENT de la tabla `Notificaciones`
--
ALTER TABLE `Notificaciones`
  MODIFY `ID_Notificaciones` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `Provincias`
--
ALTER TABLE `Provincias`
  MODIFY `ID_Provincia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT de la tabla `Publicaciones`
--
ALTER TABLE `Publicaciones`
  MODIFY `ID_Publicacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT de la tabla `Soporte`
--
ALTER TABLE `Soporte`
  MODIFY `ID_Soporte` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT de la tabla `Tipo Objetos`
--
ALTER TABLE `Tipo Objetos`
  MODIFY `ID_TipoObjeto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT de la tabla `Tipo Publicaciones`
--
ALTER TABLE `Tipo Publicaciones`
  MODIFY `ID_TipoPublicacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `Tipo Usuarios`
--
ALTER TABLE `Tipo Usuarios`
  MODIFY `ID_TipoUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT de la tabla `Usuarios`
--
ALTER TABLE `Usuarios`
  MODIFY `ID_Usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
