-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Εξυπηρετητής: 127.0.0.1
-- Χρόνος δημιουργίας: 17 Μάη 2022 στις 08:43:16
-- Έκδοση διακομιστή: 10.4.22-MariaDB
-- Έκδοση PHP: 8.1.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Βάση δεδομένων: `hy351`
--

DELIMITER $$
--
-- Διαδικασίες
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddAppointment` (IN `citizenSSN` VARCHAR(11), IN `doseNumber` INT, IN `appointmentDate` DATE, IN `appointmentTime` ENUM('08:00-12:00','12:00-16:00','16:00-20:00'), IN `vaccineName` VARCHAR(20))  BEGIN
	INSERT INTO `appointment`(`citizenSSN`, `doseNumber`, `appointmentDate`, `appointmentTime`, `vaccineName`) 
	VALUES (citizenSSN, doseNumber, appointmentDate, appointmentTime, vaccineName);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `BookAppointment` (IN `doseNumber` INT, IN `citizenSSN` VARCHAR(11), IN `hospitalID` VARCHAR(5))  BEGIN 
	INSERT INTO `books`(`doseNumber`, `citizenSSN`, `hospitalID`) 
    VALUES (doseNumber, citizenSSN, hospitalID);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `CancelAppointment` (IN `ssn` VARCHAR(11), IN `dose` INT)  BEGIN
	DECLARE daysDifference INT;
    
    SELECT DATEDIFF(a.appointmentDate, CURRENT_DATE)
    INTO daysDifference
    FROM appointment a
    WHERE a.citizenSSN = ssn AND a.doseNumber = dose;
    
    SELECT daysDifference;
    
    IF (daysDifference >= 3) THEN
    	CALL IncrementCapacity(ssn, dose);
 		DELETE FROM appointment
        WHERE citizenSSN = ssn AND doseNumber = dose;
    	DELETE FROM books 
		WHERE citizenSSN = ssn AND doseNumber = dose;
    END IF;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `IncrementCapacity` (IN `ssn` VARCHAR(11), IN `doseNum` INT)  BEGIN
		UPDATE hospital_time_slots h
        SET h.capacity = h.capacity + 1
        WHERE h.hospitalID IN (
        	SELECT b.hospitalID
            FROM books b
            WHERE b.citizenSSN = ssn AND b.doseNumber = doseNum
        ) AND h.day IN (
        	SELECT a.appointmentDate
            FROM appointment a
            WHERE a.citizenSSN = ssn and a.doseNumber = doseNum
        ) AND h.timeSlot IN (
        	SELECT a.appointmentTime
            FROM appointment a
            WHERE a.citizenSSN = ssn and a.doseNumber = doseNum
        );
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `IssueVaccinationCertificate` (`id` VARCHAR(20), `ssn` VARCHAR(11))  BEGIN
    INSERT INTO `vaccinationcerificate`(`certificateID`, `citizenSSN`) 
    VALUES (id, ssn);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ModifyDate` (IN `ssn` VARCHAR(11), IN `dose` INT, IN `newDate` DATE, IN `oldTimeSlot` ENUM('08:00-12:00','12:00-16:00','16:00-20:00'), IN `hospitalID` VARCHAR(5))  BEGIN 
	CALL IncrementCapacity(ssn , dose);
    
    UPDATE appointment
    SET appointment.appointmentDate = newDate
    WHERE appointment.citizenSSN = ssn AND appointment.doseNumber = dose;
    
    CALL UpdateCapacity(hospitalID, newDate, oldTimeSlot);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ModifyTimeSlot` (IN `ssn` VARCHAR(11), IN `dose` INT, IN `oldDate` DATE, IN `newTimeSlot` ENUM('08:00-12:00','12:00-16:00','16:00-20:00'), IN `hospitalID` VARCHAR(5))  BEGIN 
	CALL IncrementCapacity(ssn , dose);
    
    UPDATE appointment
    SET appointment.appointmentTime = newTimeSlot
    WHERE appointment.citizenSSN = ssn AND appointment.doseNumber = dose;
    
    CALL UpdateCapacity(hospitalID, oldDate, newTimeSlot);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateCapacity` (IN `hospitalID` VARCHAR(5), IN `day` DATE, IN `timeSlot` ENUM('08:00-12:00','12:00-16:00','16:00-20:00'))  BEGIN 
	DECLARE cap INT;
	SELECT capacity
    INTO cap
    FROM hospital_time_slots
    WHERE hospital_time_slots.hospitalID = hospitalID and hospital_time_slots.day = day and hospital_time_slots.timeSlot = timeSlot;
    
    IF (cap > 0) THEN
    	UPDATE hospital_time_slots
    	SET capacity = capacity - 1
        WHERE hospital_time_slots.hospitalID = hospitalID and hospital_time_slots.day = day and hospital_time_slots.timeSlot = timeSlot;
    END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateDosesAndState` (IN `ssn` VARCHAR(11), IN `doseNum` INT)  BEGIN
	DECLARE maxDoses INT;
    
	SELECT v.dosesRequired
    INTO maxDoses
    FROM Vaccine v
    JOIN hospital_vaccine hv ON hv.vaccineID = v.vaccineID
    JOIN books b ON b.hospitalID = hv.hospitalID and b.citizenSSN = ssn AND B.doseNumber = doseNum;
    
    SELECT maxDoses;
    
    IF (maxDoses = 2) THEN 
    	IF (doseNum = 1) THEN
        	UPDATE citizen
            SET dosesCompleted = doseNum, vaccinationState = 'partially vaccinated'
            WHERE citizen.SSN = ssn;
        ELSEIF (doseNum = 2) THEN
        	UPDATE citizen
            SET dosesCompleted = doseNum, vaccinationState = 'fully vaccinated'
            WHERE citizen.SSN = ssn;
        END IF;
     ELSE 
     	UPDATE citizen
        SET dosesCompleted = doseNum, vaccinationState = 'fully vaccinated'
        WHERE citizen.SSN = ssn;
     END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ViewBookedAppointments` (IN `ssn` VARCHAR(11))  BEGIN	
    SELECT b.citizenSSN, b.doseNumber, a.appointmentDate, a.appointmentTime, h.name
    FROM books b, appointment a, hospital h
    WHERE b.citizenSSN = ssn and a.citizenSSN = b.citizenSSN and h.hospitalID = b.hospitalID;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ViewDailyAppointments` (IN `hospitalID` VARCHAR(5), IN `currDate` DATE)  BEGIN
	DECLARE apptDate date;
    set apptDate = IFNULL(currDate, CURRENT_DATE);
	
    SELECT c.SSN, c.firstName, c.lastName, b.doseNumber, a.appointmentDate, a.appointmentTime, h.hospitalID
    FROM citizen c
    JOIN books b on c.SSN = b.citizenSSN
    JOIN appointment a on a.citizenSSN = b.citizenSSN and a.doseNumber = b.doseNumber and a.appointmentDate = apptDate
    JOIN hospital h on h.hospitalID = b.hospitalID
    ORDER BY a.appointmentTime ASC;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `administrator`
--

CREATE TABLE `administrator` (
  `firstName` varchar(20) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  `administratorID` varchar(5) NOT NULL,
  `hospitalID` varchar(5) DEFAULT NULL,
  `department` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `administrator`
--

INSERT INTO `administrator` (`firstName`, `lastName`, `administratorID`, `hospitalID`, `department`) VALUES
('Miguel', 'Tran', '25400', '49300', 'Nursing'),
('Brandy', 'Chambers', '45312', '16390', 'Medical'),
('Deborah', 'Myers', '84621', '20996', 'Medical'),
('Pauline', 'Rose', '86224', '81648', 'Nursing'),
('Neil', 'Davidson', '88806', '93714', 'Nursing'),
('Jennie', 'Benson', '92405', '20309', 'Medical');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `appointment`
--

CREATE TABLE `appointment` (
  `citizenSSN` varchar(11) NOT NULL,
  `doseNumber` int(11) NOT NULL,
  `appointmentDate` date DEFAULT NULL,
  `appointmentTime` enum('08:00-12:00','12:00-16:00','16:00-20:00') DEFAULT NULL,
  `vaccineName` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `appointment`
--

INSERT INTO `appointment` (`citizenSSN`, `doseNumber`, `appointmentDate`, `appointmentTime`, `vaccineName`) VALUES
('03027412995', 1, '2022-05-19', '08:00-12:00', 'Pfizer'),
('06067500198', 1, '2022-05-19', '12:00-16:00', 'Pfizer'),
('07049610092', 1, '2022-05-19', '08:00-12:00', 'Pfizer'),
('09108078512', 1, '2022-05-19', '08:00-12:00', 'Pfizer'),
('09118460019', 1, '2022-05-19', '16:00-20:00', 'Pfizer'),
('11017426134', 1, '2022-05-19', '12:00-16:00', 'Pfizer'),
('11018701926', 1, '2022-05-25', '16:00-20:00', 'Pfizer'),
('11018701926', 2, '2022-05-26', '08:00-12:00', 'Pfizer'),
('12345678900', 1, '2022-05-14', '08:00-12:00', 'Pfizer');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `books`
--

CREATE TABLE `books` (
  `doseNumber` int(11) NOT NULL,
  `citizenSSN` varchar(11) NOT NULL,
  `hospitalID` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `books`
--

INSERT INTO `books` (`doseNumber`, `citizenSSN`, `hospitalID`) VALUES
(1, '03027412995', '20309'),
(1, '06067500198', '20309'),
(1, '07049610092', '20309'),
(1, '09108078512', '20309'),
(1, '09118460019', '20309'),
(1, '11017426134', '20309'),
(1, '11018701926', '20309'),
(1, '12345678900', '20309'),
(2, '11018701926', '20309');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `citizen`
--

CREATE TABLE `citizen` (
  `firstName` varchar(20) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  `SSN` varchar(11) NOT NULL,
  `gender` enum('male','female','nb') DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `vaccinationState` enum('not vaccinated','partially vaccinated','fully vaccinated') DEFAULT 'not vaccinated',
  `dosesCompleted` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `citizen`
--

INSERT INTO `citizen` (`firstName`, `lastName`, `SSN`, `gender`, `dateOfBirth`, `vaccinationState`, `dosesCompleted`) VALUES
('Angelo', 'Morris', '02010000992', 'male', '2000-01-02', 'not vaccinated', 0),
('Mareli', 'Bender', '02024511750', 'male', '1945-02-02', 'not vaccinated', 0),
('Leonardo', 'Ferrell', '03027412995', 'male', '1974-02-03', 'not vaccinated', 0),
('Cherish', 'Mejia', '06067500198', 'male', '1975-06-06', 'not vaccinated', 0),
('Niko', 'Curry', '07049610092', 'male', '1996-04-07', 'not vaccinated', 0),
('Ricky', 'Salinas', '09108078512', 'male', '1980-10-09', 'not vaccinated', 0),
('Jake', 'Lester', '09118460019', 'male', '1984-11-09', 'not vaccinated', 0),
('Abdiel', 'Moreno', '11017426134', 'male', '1974-01-11', 'not vaccinated', 0),
('Chace', 'Downs', '11018009871', 'male', '1980-01-11', 'not vaccinated', 0),
('Kenna', 'Manning', '11018701926', 'female', '1987-01-11', 'fully vaccinated', 2),
('Leland', 'Chapman', '12109200956', 'female', '1992-10-12', 'not vaccinated', 0),
('Ricky', 'Salinas', '13117690374', 'male', '1976-11-13', 'not vaccinated', 0),
('Aubrey', 'Proctor', '15059775840', 'female', '1997-05-15', 'not vaccinated', 0),
('Dean', 'Arellano', '16096536000', 'male', '1965-09-16', 'not vaccinated', 0),
('Fernanda', 'Ali', '18079318729', 'female', '1993-07-18', 'not vaccinated', 0),
('Brycen', 'Valentine', '18090119872', 'male', '2001-09-18', 'not vaccinated', 0),
('Mckinley', 'Bush', '25036650948', 'male', '1966-03-25', 'not vaccinated', 0),
('Jessica', 'Deleon', '26055743567', 'female', '1957-05-26', 'not vaccinated', 0),
('Kyle', 'Dalton', '26116473859', 'male', '1964-11-26', 'not vaccinated', 0),
('Jayden', 'Ortiz', '27108645329', 'female', '1986-10-27', 'not vaccinated', 0),
('Roger', 'Villegas', '29056154620', 'male', '1961-05-29', 'not vaccinated', 0);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `hospital`
--

CREATE TABLE `hospital` (
  `hospitalID` varchar(5) NOT NULL,
  `administratorID` varchar(5) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `address` varchar(30) DEFAULT NULL,
  `postalCode` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `hospital`
--

INSERT INTO `hospital` (`hospitalID`, `administratorID`, `name`, `address`, `postalCode`) VALUES
('16390', '45312', 'Thessaloniki Hospita', '106 Garrison Ct', '38013'),
('20309', '92405', 'Heraklion Hospital', '4478 County St', '21809'),
('20996', '84621', 'Athens Hospital', '8 Port Colden Rd', '70328'),
('25718', '79394', 'Volos Hospital', '26 Midland Ave', '27190'),
('49300', '25400', 'Patras Hospital', '140 Robinwood Ln', '56067'),
('81648', '86224', 'Chania Hospital', '4478 County St', '62425'),
('93714', '88806', 'Ioannina Hospital', '239 Poplar Ridge Rd', '33832');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `hospital_time_slots`
--

CREATE TABLE `hospital_time_slots` (
  `hospitalID` varchar(5) DEFAULT NULL,
  `day` date DEFAULT NULL,
  `timeSlot` enum('08:00-12:00','12:00-16:00','16:00-20:00') DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `hospital_time_slots`
--

INSERT INTO `hospital_time_slots` (`hospitalID`, `day`, `timeSlot`, `capacity`) VALUES
('20309', '2022-05-16', '08:00-12:00', 6),
('20309', '2022-05-16', '12:00-16:00', 6),
('20309', '2022-05-16', '16:00-20:00', 6),
('20309', '2022-05-17', '08:00-12:00', 6),
('20309', '2022-05-17', '12:00-16:00', 6),
('20309', '2022-05-17', '16:00-20:00', 6),
('20309', '2022-05-18', '08:00-12:00', 6),
('20309', '2022-05-18', '12:00-16:00', 6),
('20309', '2022-05-18', '16:00-20:00', 6),
('20309', '2022-05-19', '08:00-12:00', 6),
('20309', '2022-05-19', '12:00-16:00', 6),
('20309', '2022-05-19', '16:00-20:00', 6),
('20309', '2022-05-20', '08:00-12:00', 6),
('20309', '2022-05-20', '12:00-16:00', 6),
('20309', '2022-05-20', '16:00-20:00', 6),
('20309', '2022-05-21', '08:00-12:00', 6),
('20309', '2022-05-21', '12:00-16:00', 6),
('20309', '2022-05-21', '16:00-20:00', 6),
('20309', '2022-05-22', '08:00-12:00', 6),
('20309', '2022-05-22', '12:00-16:00', 6),
('20309', '2022-05-22', '16:00-20:00', 6),
('20309', '2022-05-23', '08:00-12:00', 6),
('20309', '2022-05-23', '12:00-16:00', 6),
('20309', '2022-05-23', '16:00-20:00', 6),
('20309', '2022-05-24', '08:00-12:00', 6),
('20309', '2022-05-24', '12:00-16:00', 6),
('20309', '2022-05-24', '16:00-20:00', 6),
('20309', '2022-05-25', '08:00-12:00', 6),
('20309', '2022-05-25', '12:00-16:00', 6),
('20309', '2022-05-25', '16:00-20:00', 5),
('20309', '2022-05-26', '08:00-12:00', 5),
('20309', '2022-05-26', '12:00-16:00', 6),
('20309', '2022-05-26', '16:00-20:00', 6),
('20309', '2022-05-27', '08:00-12:00', 6),
('20309', '2022-05-27', '12:00-16:00', 6),
('20309', '2022-05-27', '16:00-20:00', 6),
('20309', '2022-05-28', '08:00-12:00', 6),
('20309', '2022-05-28', '12:00-16:00', 6),
('20309', '2022-05-28', '16:00-20:00', 6),
('20309', '2022-05-29', '08:00-12:00', 6),
('20309', '2022-05-29', '12:00-16:00', 6),
('20309', '2022-05-29', '16:00-20:00', 6),
('20309', '2022-05-30', '08:00-12:00', 6),
('20309', '2022-05-30', '12:00-16:00', 6),
('20309', '2022-05-30', '16:00-20:00', 6),
('20309', '2022-05-31', '08:00-12:00', 6),
('20309', '2022-05-31', '12:00-16:00', 6),
('20309', '2022-05-31', '16:00-20:00', 6),
('16390', '2022-05-16', '08:00-12:00', 5),
('16390', '2022-05-16', '12:00-16:00', 5),
('16390', '2022-05-16', '16:00-20:00', 5),
('16390', '2022-05-17', '08:00-12:00', 5),
('16390', '2022-05-17', '12:00-16:00', 5),
('16390', '2022-05-17', '16:00-20:00', 5),
('16390', '2022-05-18', '08:00-12:00', 5),
('16390', '2022-05-18', '12:00-16:00', 5),
('16390', '2022-05-18', '16:00-20:00', 5),
('16390', '2022-05-19', '08:00-12:00', 5),
('16390', '2022-05-19', '12:00-16:00', 5),
('16390', '2022-05-19', '16:00-20:00', 5),
('16390', '2022-05-20', '08:00-12:00', 5),
('16390', '2022-05-20', '12:00-16:00', 5),
('16390', '2022-05-20', '16:00-20:00', 5),
('16390', '2022-05-21', '08:00-12:00', 5),
('16390', '2022-05-21', '12:00-16:00', 5),
('16390', '2022-05-21', '16:00-20:00', 5),
('16390', '2022-05-22', '08:00-12:00', 5),
('16390', '2022-05-22', '12:00-16:00', 5),
('16390', '2022-05-22', '16:00-20:00', 5),
('16390', '2022-05-23', '08:00-12:00', 5),
('16390', '2022-05-23', '12:00-16:00', 5),
('16390', '2022-05-23', '16:00-20:00', 5),
('16390', '2022-05-24', '08:00-12:00', 5),
('16390', '2022-05-24', '12:00-16:00', 5),
('16390', '2022-05-24', '16:00-20:00', 5),
('16390', '2022-05-25', '08:00-12:00', 5),
('16390', '2022-05-25', '12:00-16:00', 5),
('16390', '2022-05-25', '16:00-20:00', 5),
('16390', '2022-05-26', '08:00-12:00', 5),
('16390', '2022-05-26', '12:00-16:00', 5),
('16390', '2022-05-26', '16:00-20:00', 5),
('16390', '2022-05-27', '08:00-12:00', 5),
('16390', '2022-05-27', '12:00-16:00', 5),
('16390', '2022-05-27', '16:00-20:00', 5),
('16390', '2022-05-28', '08:00-12:00', 5),
('16390', '2022-05-28', '12:00-16:00', 5),
('16390', '2022-05-28', '16:00-20:00', 5),
('16390', '2022-05-29', '08:00-12:00', 5),
('16390', '2022-05-29', '12:00-16:00', 5),
('16390', '2022-05-29', '16:00-20:00', 5),
('16390', '2022-05-30', '08:00-12:00', 5),
('16390', '2022-05-30', '12:00-16:00', 5),
('16390', '2022-05-30', '16:00-20:00', 5),
('16390', '2022-05-31', '08:00-12:00', 5),
('16390', '2022-05-31', '12:00-16:00', 5),
('16390', '2022-05-31', '16:00-20:00', 5),
('20996', '2022-05-16', '08:00-12:00', 5),
('20996', '2022-05-16', '12:00-16:00', 5),
('20996', '2022-05-16', '16:00-20:00', 5),
('20996', '2022-05-17', '08:00-12:00', 5),
('20996', '2022-05-17', '12:00-16:00', 5),
('20996', '2022-05-17', '16:00-20:00', 5),
('20996', '2022-05-18', '08:00-12:00', 5),
('20996', '2022-05-18', '12:00-16:00', 5),
('20996', '2022-05-18', '16:00-20:00', 5),
('20996', '2022-05-19', '08:00-12:00', 5),
('20996', '2022-05-19', '12:00-16:00', 5),
('20996', '2022-05-19', '16:00-20:00', 5),
('20996', '2022-05-20', '08:00-12:00', 5),
('20996', '2022-05-20', '12:00-16:00', 5),
('20996', '2022-05-20', '16:00-20:00', 5),
('20996', '2022-05-21', '08:00-12:00', 5),
('20996', '2022-05-21', '12:00-16:00', 5),
('20996', '2022-05-21', '16:00-20:00', 5),
('20996', '2022-05-22', '08:00-12:00', 5),
('20996', '2022-05-22', '12:00-16:00', 5),
('20996', '2022-05-22', '16:00-20:00', 5),
('20996', '2022-05-23', '08:00-12:00', 5),
('20996', '2022-05-23', '12:00-16:00', 5),
('20996', '2022-05-23', '16:00-20:00', 5),
('20996', '2022-05-24', '08:00-12:00', 5),
('20996', '2022-05-24', '12:00-16:00', 5),
('20996', '2022-05-24', '16:00-20:00', 5),
('20996', '2022-05-25', '08:00-12:00', 5),
('20996', '2022-05-25', '12:00-16:00', 5),
('20996', '2022-05-25', '16:00-20:00', 5),
('20996', '2022-05-26', '08:00-12:00', 5),
('20996', '2022-05-26', '12:00-16:00', 5),
('20996', '2022-05-26', '16:00-20:00', 5),
('20996', '2022-05-27', '08:00-12:00', 5),
('20996', '2022-05-27', '12:00-16:00', 5),
('20996', '2022-05-27', '16:00-20:00', 5),
('20996', '2022-05-28', '08:00-12:00', 5),
('20996', '2022-05-28', '12:00-16:00', 5),
('20996', '2022-05-28', '16:00-20:00', 5),
('20996', '2022-05-29', '08:00-12:00', 5),
('20996', '2022-05-29', '12:00-16:00', 5),
('20996', '2022-05-29', '16:00-20:00', 5),
('20996', '2022-05-30', '08:00-12:00', 5),
('20996', '2022-05-30', '12:00-16:00', 5),
('20996', '2022-05-30', '16:00-20:00', 5),
('20996', '2022-05-31', '08:00-12:00', 5),
('20996', '2022-05-31', '12:00-16:00', 5),
('20996', '2022-05-31', '16:00-20:00', 5),
('25718', '2022-05-16', '08:00-12:00', 5),
('25718', '2022-05-16', '12:00-16:00', 5),
('25718', '2022-05-16', '16:00-20:00', 5),
('25718', '2022-05-17', '08:00-12:00', 5),
('25718', '2022-05-17', '12:00-16:00', 5),
('25718', '2022-05-17', '16:00-20:00', 5),
('25718', '2022-05-18', '08:00-12:00', 5),
('25718', '2022-05-18', '12:00-16:00', 5),
('25718', '2022-05-18', '16:00-20:00', 5),
('25718', '2022-05-19', '08:00-12:00', 5),
('25718', '2022-05-19', '12:00-16:00', 5),
('25718', '2022-05-19', '16:00-20:00', 5),
('25718', '2022-05-20', '08:00-12:00', 5),
('25718', '2022-05-20', '12:00-16:00', 5),
('25718', '2022-05-20', '16:00-20:00', 5),
('25718', '2022-05-21', '08:00-12:00', 5),
('25718', '2022-05-21', '12:00-16:00', 5),
('25718', '2022-05-21', '16:00-20:00', 5),
('25718', '2022-05-22', '08:00-12:00', 5),
('25718', '2022-05-22', '12:00-16:00', 5),
('25718', '2022-05-22', '16:00-20:00', 5),
('25718', '2022-05-23', '08:00-12:00', 5),
('25718', '2022-05-23', '12:00-16:00', 5),
('25718', '2022-05-23', '16:00-20:00', 5),
('25718', '2022-05-24', '08:00-12:00', 5),
('25718', '2022-05-24', '12:00-16:00', 5),
('25718', '2022-05-24', '16:00-20:00', 5),
('25718', '2022-05-25', '08:00-12:00', 5),
('25718', '2022-05-25', '12:00-16:00', 5),
('25718', '2022-05-25', '16:00-20:00', 5),
('25718', '2022-05-26', '08:00-12:00', 5),
('25718', '2022-05-26', '12:00-16:00', 5),
('25718', '2022-05-26', '16:00-20:00', 5),
('25718', '2022-05-27', '08:00-12:00', 5),
('25718', '2022-05-27', '12:00-16:00', 5),
('25718', '2022-05-27', '16:00-20:00', 5),
('25718', '2022-05-28', '08:00-12:00', 5),
('25718', '2022-05-28', '12:00-16:00', 5),
('25718', '2022-05-28', '16:00-20:00', 5),
('25718', '2022-05-29', '08:00-12:00', 5),
('25718', '2022-05-29', '12:00-16:00', 5),
('25718', '2022-05-29', '16:00-20:00', 5),
('25718', '2022-05-30', '08:00-12:00', 5),
('25718', '2022-05-30', '12:00-16:00', 5),
('25718', '2022-05-30', '16:00-20:00', 5),
('25718', '2022-05-31', '08:00-12:00', 5),
('25718', '2022-05-31', '12:00-16:00', 5),
('25718', '2022-05-31', '16:00-20:00', 5),
('49300', '2022-05-16', '08:00-12:00', 5),
('49300', '2022-05-16', '12:00-16:00', 5),
('49300', '2022-05-16', '16:00-20:00', 5),
('49300', '2022-05-17', '08:00-12:00', 5),
('49300', '2022-05-17', '12:00-16:00', 5),
('49300', '2022-05-17', '16:00-20:00', 5),
('49300', '2022-05-18', '08:00-12:00', 5),
('49300', '2022-05-18', '12:00-16:00', 5),
('49300', '2022-05-18', '16:00-20:00', 5),
('49300', '2022-05-19', '08:00-12:00', 5),
('49300', '2022-05-19', '12:00-16:00', 5),
('49300', '2022-05-19', '16:00-20:00', 5),
('49300', '2022-05-20', '08:00-12:00', 5),
('49300', '2022-05-20', '12:00-16:00', 5),
('49300', '2022-05-20', '16:00-20:00', 5),
('49300', '2022-05-21', '08:00-12:00', 5),
('49300', '2022-05-21', '12:00-16:00', 5),
('49300', '2022-05-21', '16:00-20:00', 5),
('49300', '2022-05-22', '08:00-12:00', 5),
('49300', '2022-05-22', '12:00-16:00', 5),
('49300', '2022-05-22', '16:00-20:00', 5),
('49300', '2022-05-23', '08:00-12:00', 5),
('49300', '2022-05-23', '12:00-16:00', 5),
('49300', '2022-05-23', '16:00-20:00', 5),
('49300', '2022-05-24', '08:00-12:00', 5),
('49300', '2022-05-24', '12:00-16:00', 5),
('49300', '2022-05-24', '16:00-20:00', 5),
('49300', '2022-05-25', '08:00-12:00', 5),
('49300', '2022-05-25', '12:00-16:00', 5),
('49300', '2022-05-25', '16:00-20:00', 5),
('49300', '2022-05-26', '08:00-12:00', 5),
('49300', '2022-05-26', '12:00-16:00', 5),
('49300', '2022-05-26', '16:00-20:00', 5),
('49300', '2022-05-27', '08:00-12:00', 5),
('49300', '2022-05-27', '12:00-16:00', 5),
('49300', '2022-05-27', '16:00-20:00', 5),
('49300', '2022-05-28', '08:00-12:00', 5),
('49300', '2022-05-28', '12:00-16:00', 5),
('49300', '2022-05-28', '16:00-20:00', 5),
('49300', '2022-05-29', '08:00-12:00', 5),
('49300', '2022-05-29', '12:00-16:00', 5),
('49300', '2022-05-29', '16:00-20:00', 5),
('49300', '2022-05-30', '08:00-12:00', 5),
('49300', '2022-05-30', '12:00-16:00', 5),
('49300', '2022-05-30', '16:00-20:00', 5),
('49300', '2022-05-31', '08:00-12:00', 5),
('49300', '2022-05-31', '12:00-16:00', 5),
('49300', '2022-05-31', '16:00-20:00', 5),
('81648', '2022-05-16', '08:00-12:00', 5),
('81648', '2022-05-16', '12:00-16:00', 5),
('81648', '2022-05-16', '16:00-20:00', 5),
('81648', '2022-05-17', '08:00-12:00', 5),
('81648', '2022-05-17', '12:00-16:00', 5),
('81648', '2022-05-17', '16:00-20:00', 5),
('81648', '2022-05-18', '08:00-12:00', 5),
('81648', '2022-05-18', '12:00-16:00', 5),
('81648', '2022-05-18', '16:00-20:00', 5),
('81648', '2022-05-19', '08:00-12:00', 5),
('81648', '2022-05-19', '12:00-16:00', 5),
('81648', '2022-05-19', '16:00-20:00', 5),
('81648', '2022-05-20', '08:00-12:00', 5),
('81648', '2022-05-20', '12:00-16:00', 5),
('81648', '2022-05-20', '16:00-20:00', 5),
('81648', '2022-05-21', '08:00-12:00', 5),
('81648', '2022-05-21', '12:00-16:00', 5),
('81648', '2022-05-21', '16:00-20:00', 5),
('81648', '2022-05-22', '08:00-12:00', 5),
('81648', '2022-05-22', '12:00-16:00', 5),
('81648', '2022-05-22', '16:00-20:00', 5),
('81648', '2022-05-23', '08:00-12:00', 5),
('81648', '2022-05-23', '12:00-16:00', 5),
('81648', '2022-05-23', '16:00-20:00', 5),
('81648', '2022-05-24', '08:00-12:00', 5),
('81648', '2022-05-24', '12:00-16:00', 5),
('81648', '2022-05-24', '16:00-20:00', 5),
('81648', '2022-05-25', '08:00-12:00', 5),
('81648', '2022-05-25', '12:00-16:00', 5),
('81648', '2022-05-25', '16:00-20:00', 5),
('81648', '2022-05-26', '08:00-12:00', 5),
('81648', '2022-05-26', '12:00-16:00', 5),
('81648', '2022-05-26', '16:00-20:00', 5),
('81648', '2022-05-27', '08:00-12:00', 5),
('81648', '2022-05-27', '12:00-16:00', 5),
('81648', '2022-05-27', '16:00-20:00', 5),
('81648', '2022-05-28', '08:00-12:00', 5),
('81648', '2022-05-28', '12:00-16:00', 5),
('81648', '2022-05-28', '16:00-20:00', 5),
('81648', '2022-05-29', '08:00-12:00', 5),
('81648', '2022-05-29', '12:00-16:00', 5),
('81648', '2022-05-29', '16:00-20:00', 5),
('81648', '2022-05-30', '08:00-12:00', 5),
('81648', '2022-05-30', '12:00-16:00', 5),
('81648', '2022-05-30', '16:00-20:00', 5),
('81648', '2022-05-31', '08:00-12:00', 5),
('81648', '2022-05-31', '12:00-16:00', 5),
('81648', '2022-05-31', '16:00-20:00', 5),
('93714', '2022-05-16', '08:00-12:00', 5),
('93714', '2022-05-16', '12:00-16:00', 5),
('93714', '2022-05-16', '16:00-20:00', 5),
('93714', '2022-05-17', '08:00-12:00', 5),
('93714', '2022-05-17', '12:00-16:00', 5),
('93714', '2022-05-17', '16:00-20:00', 5),
('93714', '2022-05-18', '08:00-12:00', 5),
('93714', '2022-05-18', '12:00-16:00', 5),
('93714', '2022-05-18', '16:00-20:00', 5),
('93714', '2022-05-19', '08:00-12:00', 5),
('93714', '2022-05-19', '12:00-16:00', 5),
('93714', '2022-05-19', '16:00-20:00', 5),
('93714', '2022-05-20', '08:00-12:00', 5),
('93714', '2022-05-20', '12:00-16:00', 5),
('93714', '2022-05-20', '16:00-20:00', 5),
('93714', '2022-05-21', '08:00-12:00', 5),
('93714', '2022-05-21', '12:00-16:00', 5),
('93714', '2022-05-21', '16:00-20:00', 5),
('93714', '2022-05-22', '08:00-12:00', 5),
('93714', '2022-05-22', '12:00-16:00', 5),
('93714', '2022-05-22', '16:00-20:00', 5),
('93714', '2022-05-23', '08:00-12:00', 5),
('93714', '2022-05-23', '12:00-16:00', 5),
('93714', '2022-05-23', '16:00-20:00', 5),
('93714', '2022-05-24', '08:00-12:00', 5),
('93714', '2022-05-24', '12:00-16:00', 5),
('93714', '2022-05-24', '16:00-20:00', 5),
('93714', '2022-05-25', '08:00-12:00', 5),
('93714', '2022-05-25', '12:00-16:00', 5),
('93714', '2022-05-25', '16:00-20:00', 5),
('93714', '2022-05-26', '08:00-12:00', 5),
('93714', '2022-05-26', '12:00-16:00', 5),
('93714', '2022-05-26', '16:00-20:00', 5),
('93714', '2022-05-27', '08:00-12:00', 5),
('93714', '2022-05-27', '12:00-16:00', 5),
('93714', '2022-05-27', '16:00-20:00', 5),
('93714', '2022-05-28', '08:00-12:00', 5),
('93714', '2022-05-28', '12:00-16:00', 5),
('93714', '2022-05-28', '16:00-20:00', 5),
('93714', '2022-05-29', '08:00-12:00', 5),
('93714', '2022-05-29', '12:00-16:00', 5),
('93714', '2022-05-29', '16:00-20:00', 5),
('93714', '2022-05-30', '08:00-12:00', 5),
('93714', '2022-05-30', '12:00-16:00', 5),
('93714', '2022-05-30', '16:00-20:00', 5),
('93714', '2022-05-31', '08:00-12:00', 5),
('93714', '2022-05-31', '12:00-16:00', 5),
('93714', '2022-05-31', '16:00-20:00', 5);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `hospital_vaccine`
--

CREATE TABLE `hospital_vaccine` (
  `hospitalID` varchar(5) NOT NULL,
  `vaccineID` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `hospital_vaccine`
--

INSERT INTO `hospital_vaccine` (`hospitalID`, `vaccineID`) VALUES
('16390', '8445'),
('20309', '8445'),
('20996', '7546'),
('25718', '3498'),
('49300', '8445'),
('81648', '8445'),
('93714', '3498');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `medicalstaff`
--

CREATE TABLE `medicalstaff` (
  `firstName` varchar(20) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  `employeeID` varchar(5) NOT NULL,
  `hospitalID` varchar(5) NOT NULL,
  `department` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `medicalstaff`
--

INSERT INTO `medicalstaff` (`firstName`, `lastName`, `employeeID`, `hospitalID`, `department`) VALUES
('Marie', 'Hall', '13299', '20309', 'Nursing'),
('Jonathan', 'Hughes', '17681', '16390', 'Nursing'),
('Charles', 'Watson', '34486', '93714', 'Medical'),
('Larry', 'Johnson', '34597', '20996', 'Nursing'),
('Ernest', 'Ross', '37204', '93714', 'Nursing'),
('Judy', 'Baker', '38172', '81648', 'Nursing'),
('Mary', 'Coleman', '44433', '81648', 'Medical'),
('Keith', 'Young', '46624', '20996', 'Nursing'),
('Evelyn', 'Allen', '47983', '93714', 'Medical'),
('Mildred', 'Kelly', '48837', '20309', 'Nursing'),
('Edward', 'Cook', '49012', '16390', 'Medical'),
('Douglas', 'Jackson', '49300', '98981', 'Nursing'),
('Alice', 'Alexander', '53225', '20996', 'Medical'),
('Annie', 'Harris', '53904', '81648', 'Medical'),
('Ashley', 'Jenkins', '55708', '20996', 'Medical'),
('Richard', 'Carter', '63640', '49300', 'Medical'),
('Donald', 'Foster', '70171', '81648', 'Nursing'),
('Adam', 'Clark', '71308', '20309', 'Medical'),
('Heather', 'Wood', '81383', '49300', 'Medical'),
('Walter', 'Patterson', '85710', '16390', 'Medical'),
('Fred', 'Evans', '89706', '20996', 'Medical'),
('Jack', 'Ramirez', '92700', '20309', 'Medical'),
('Matthew', 'Scott', '94412', '49300', 'Medical'),
('Timothy', 'Bell', '97529', '16390', 'Medical'),
('Ruth', 'Hill', '98695', '93714', 'Nursing'),
('Douglas', 'Jackson', '98981', '49300', 'Nursing');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `vaccinationcerificate`
--

CREATE TABLE `vaccinationcerificate` (
  `certificateID` varchar(20) NOT NULL,
  `citizenSSN` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `vaccinationcerificate`
--

INSERT INTO `vaccinationcerificate` (`certificateID`, `citizenSSN`) VALUES
('11018701926', '1');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `vaccine`
--

CREATE TABLE `vaccine` (
  `name` varchar(20) DEFAULT NULL,
  `vaccineID` varchar(5) NOT NULL,
  `manufacturer` varchar(30) DEFAULT NULL,
  `dosesRequired` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Άδειασμα δεδομένων του πίνακα `vaccine`
--

INSERT INTO `vaccine` (`name`, `vaccineID`, `manufacturer`, `dosesRequired`, `quantity`) VALUES
('Johnson', '3498', 'Johnson & Johnson', 1, 90000),
('Moderna', '7546', 'Moderna Biotech', 2, 20000),
('Pfizer', '8445', 'Pfizer - BioNTech', 2, 100000);

--
-- Ευρετήρια για άχρηστους πίνακες
--

--
-- Ευρετήρια για πίνακα `administrator`
--
ALTER TABLE `administrator`
  ADD PRIMARY KEY (`administratorID`);

--
-- Ευρετήρια για πίνακα `appointment`
--
ALTER TABLE `appointment`
  ADD PRIMARY KEY (`citizenSSN`,`doseNumber`);

--
-- Ευρετήρια για πίνακα `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`doseNumber`,`citizenSSN`);

--
-- Ευρετήρια για πίνακα `citizen`
--
ALTER TABLE `citizen`
  ADD PRIMARY KEY (`SSN`),
  ADD UNIQUE KEY `SSN` (`SSN`);

--
-- Ευρετήρια για πίνακα `hospital`
--
ALTER TABLE `hospital`
  ADD PRIMARY KEY (`hospitalID`);

--
-- Ευρετήρια για πίνακα `hospital_vaccine`
--
ALTER TABLE `hospital_vaccine`
  ADD PRIMARY KEY (`hospitalID`);

--
-- Ευρετήρια για πίνακα `medicalstaff`
--
ALTER TABLE `medicalstaff`
  ADD PRIMARY KEY (`employeeID`,`hospitalID`);

--
-- Ευρετήρια για πίνακα `vaccinationcerificate`
--
ALTER TABLE `vaccinationcerificate`
  ADD PRIMARY KEY (`certificateID`);

--
-- Ευρετήρια για πίνακα `vaccine`
--
ALTER TABLE `vaccine`
  ADD PRIMARY KEY (`vaccineID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
