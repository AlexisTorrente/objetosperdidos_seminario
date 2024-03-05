SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


DELIMITER $$
CREATE DEFINER=`southern_TP_FINAL_TSSI_G4`@`%` PROCEDURE `ObtenerLocalidadConMasPublicaciones` ()  BEGIN
    SELECT Pub.ID_Localidad, Pub.Repetido, L.Nombre
    FROM (
        SELECT ID_Localidad, COUNT(*) AS Repetido
        FROM Publicaciones
        GROUP BY ID_Localidad
        ORDER BY Repetido DESC
        LIMIT 1
    ) AS Pub
    INNER JOIN Localidades AS L 
    ON Pub.ID_Localidad = L.ID_Localidad;
END$$

CREATE DEFINER=`southern_TP_FINAL_TSSI_G4`@`%` PROCEDURE `ObtenerLocalidadConMasPublicacionesPorTipo` (IN `TipoPublicacion` INT)  BEGIN
    SELECT Pub.ID_Localidad, Pub.Repetido, L.Nombre
    FROM (
        SELECT ID_Localidad, COUNT(*) AS Repetido
        FROM Publicaciones
        WHERE ID_TipoPublicacion = TipoPublicacion
        GROUP BY ID_Localidad
        ORDER BY Repetido DESC
        LIMIT 1
    ) AS Pub
    INNER JOIN Localidades AS L 
    ON Pub.ID_Localidad = L.ID_Localidad;
END$$

CREATE DEFINER=`southern_TP_FINAL_TSSI_G4`@`%` PROCEDURE `ObtenerLocalidadConMasRecuperados` ()  BEGIN
    SELECT Pub.ID_Localidad, Pub.Repetido, L.Nombre
    FROM (
        SELECT ID_Localidad, COUNT(*) AS Repetido
        FROM Publicaciones
        WHERE Recuperado <> 0
        GROUP BY ID_Localidad
        ORDER BY Repetido DESC
        LIMIT 1
    ) AS Pub
    INNER JOIN Localidades AS L 
    ON Pub.ID_Localidad = L.ID_Localidad;
    END$$

CREATE DEFINER=`southern_TP_FINAL_TSSI_G4`@`%` PROCEDURE `ObtenerProvinciaConMasRecuperados` ()  BEGIN
    SELECT Pub.ID_Provincia, Pub.Repetido, P.Nombre
    FROM (
        SELECT ID_Provincia, COUNT(*) AS Repetido
        FROM Publicaciones
        WHERE Recuperado <> 0
        GROUP BY ID_Provincia
        ORDER BY Repetido DESC
        LIMIT 1
    ) AS Pub
    INNER JOIN Provincias AS P
    ON Pub.ID_Provincia = P.ID_Provincia;

END$$

CREATE DEFINER=`southern_TP_FINAL_TSSI_G4`@`%` PROCEDURE `ObtenerTipoObjetoConMasPublicaciones` ()  BEGIN
  SELECT Pub.ID_TipoObjeto, Pub.Repetido, tobj.Descripcion
    FROM (
        SELECT ID_TipoObjeto, COUNT(*) AS Repetido
        FROM southern_TP_FINAL_TSSI_G4.Publicaciones
        GROUP BY ID_TipoObjeto
        ORDER BY Repetido DESC
        LIMIT 1
    ) AS Pub
    INNER JOIN southern_TP_FINAL_TSSI_G4.`Tipo Objetos` AS tobj
    ON Pub.ID_TipoObjeto = tobj.ID_TipoObjeto;
END$$

DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
