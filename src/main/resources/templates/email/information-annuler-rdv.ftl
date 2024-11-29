<!doctype html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Réinitialisation confirmé</title>
</head>
<body>
<div style="max-width:650px;margin:30px auto">
    <div>
        <div style="padding-top:10px;padding-bottom:10px;margin:0;background:#fff">
            <div style="max-width:650px;margin:30px auto">
                <div style="border:1px solid #e8e8e8;padding:20px 0 0;background:#fff">
                    <div style="text-align:center;border-bottom:1px solid #e8e8e8">
                        <img src="https://cdn.tictacsante.com/assets/images/logo.png" alt="TicTAcSanté" style="margin-bottom:15px;margin-top:10px">
                    </div>
                    <table width="648" border="0" cellspacing="0" cellpadding="0" bgcolor="#656565">
                        <tbody>
                        <tr>
                            <td align="center"
                                style="padding-bottom:5px;padding-top:5px;padding-left:30px;padding-right:10px">
                                <font style="font-size:18px;font-family:Arial,sans-serif;font-weight:lighter;line-height:24px" color="#ffffff">Annulation du rendez vous</font>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div style="padding:10px 15px 10px;color:#5f5f5f;font-family:sans-serif;font-weight:100;line-height:21px;font-size:15px">
                        <table cellpadding="12" cellspacing="0" border="0" width="100%" style="font-family:Helvetica,Arial,sans-serif;width:100%!important">
                            <tbody>
                            <tr>
                                <td>
                                    <h1 style="margin:0;padding:0;font-family:Helvetica,Arial,sans-serif;color:#555555;font-size:16px">
                                        Bonjour ${NomComplet_Praticien},
                                    </h1>
                                    <p style="text-align: justify;">
                                        vous vous informer que votre patient ${NomComplet} a été annulé le rendez-vous
                                    </p>
                                <td>
                            <tr>
                                <td>

                                    <table align="left" bgcolor="#FFFFFF" width="100%" cellspacing="0" cellpadding="0" border="0" style="width:100%!important;background-color:#ffffff;color:#666666;border:1px solid #dddddd;font-family:Helvetica,arial,sans-serif;text-align:left;border-radius:3px">
                                        <tbody>
                                        <tr>
                                            <td>
                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="width:100%!important">
                                                    <tbody>
                                                    <tr>
                                                        <td align="left" style="text-align:left;padding:20px 20px 0;color:#333333;font-size:16px;font-weight:bold">
                                                            <strong>
                                                                Détails de votre rendez-vous
                                                            </strong>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table align="left" width="100%" cellspacing="0" cellpadding="0" border="0" style="width:100%!important;padding:12px 24px">
                                                                <tbody>
                                                                <tr>
                                                                    <td align="left" width="36">
                                                                        <img src="https://cdn4.iconfinder.com/data/icons/small-n-flat/24/calendar-128.png   " width="22" height="22" alt="" align="absmiddle" class="CToWUd">
                                                                    </td>
                                                                    <td align="left">
                                                                        <p style="margin:0;font-size:12px;color:#adafaf;font-weight:400">
                                                                                                  <span style="font-size:14px;line-height:14px;color:#333333">
                                                                                                   ${dayName} ${DateRdv?datetime("d")} ${monthName} ${DateRdv?datetime("Y")} à ${DateRdv?datetime("H:mm")}
                                                                                            </span>
                                                                        </p>
                                                                    </td>
                                                                </tr>


                                                                <tr>
                                                                    <td colspan="2">
                                                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" style="width:100%!important">
                                                                            <tbody>
                                                                            <tr>
                                                                                <td align="left" width="36" style="padding-top:20px">
                                                                                    <img src="http://www.exgonline.com/wp-content/plugins/EXG_Routes/icons/map/free-map-marker-icon-green-darker.png" width="22" height="22" alt="" align="absmiddle" class="CToWUd">
                                                                                </td>
                                                                                <td rowspan="2">
                                                                                    <p style="margin:0px;font-size:12px;color:#adafaf;font-weight:400">
                                                                                        Lieu<br>
                                                                                        <span style="font-size:14px;line-height:14px;color:#333333">
                                                                                                                    ${AdresseComplete}<br>
                                                                                                        ${ComplementAdresse}
                                                                                                                </span>
                                                                                    </p>
                                                                                </td>
                                                                            </tr>


                                                                            </tbody>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                </tbody>
                                                            </table>
                                                        </td>
                                                    </tr>


                                                    <tr>
                                                        <td align="center" colspan="2" style="padding:18px 0px;border-top:1px solid #dddddd;text-align:center">
                                                                                    <span style="font-size:14px">
                                                                                        <strong style="font-size:36px;color:rgb(45, 150, 151);">${DureeRdv}min</strong>
                                                                                        <br>${NomTypeRdv}
                                                                                    </span>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                                </td>
                                <td>
                                    <p>
                                        Nous vous remercions de votre confiance.
                                    </p>
                                    <p>
                                        <strong>L'Equipe TicTacSanté</strong>
                                    </p>
                                </td>
                            </tr>


                            </tbody>
                        </table>

                    </div>
                    <table border="0" cellpadding="0" cellspacing="0" style="border-radius:5px 5px 0 0;margin:0 auto;overflow:hidden;padding:0;width:100%">
                        <tbody>
                        <tr>
                            <td>
                                <table bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-top:1px solid #f0f1f2;margin:0 auto;width:100%">
                                    <tbody>
                                    <tr>
                                        <td width="16"></td>
                                        <td>
                                            <table cellpadding="0" cellspacing="0" style="width:100%">
                                                <tbody>
                                                <tr>
                                                    <td style="padding:0">
                                                        <table cellpadding="0" cellspacing="0" width="100%" style="border-bottom:1px solid #daedfc">
                                                            <tbody>
                                                            <tr>
                                                                <td style="padding:12px 0;text-align:center">
                                                                    <div style="display:inline-block;padding:20px 0;width:120px;vertical-align:top">
                                                                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                            <tbody>
                                                                            <tr>
                                                                                <td height="5" width="5" style="font-size:0;line-height:0"></td>
                                                                            </tr>
                                                                            </tbody>
                                                                        </table>
                                                                        <div style="font-family:Arial,Helvetica,sans-serif,'Roboto';font-size:16px;letter-spacing:-0.5px;font-weight:bold;color:#888888;font-weight:normal">
                                                                            Connectez-vous avec nous:
                                                                        </div>
                                                                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                            <tbody>
                                                                            <tr>
                                                                                <td height="5" width="5" style="font-size:0;line-height:0"></td>
                                                                            </tr>
                                                                            </tbody>
                                                                        </table>
                                                                    </div>
                                                                    <div style="display:inline-block;padding:20px 0;text-align:center;vertical-align:middle">
                                                                        <table align="center" border="0" cellpadding="0" cellspacing="0" style="padding:0 0 0 6px;text-align:center;width:100%">
                                                                            <tbody>
                                                                            <tr>
                                                                                <td style="display:inline-block;padding:0 4px;text-align:center">
                                                                                    <a href="https://twitter.com/TicTacSante" style="text-decoration:none" target="_blank">
                                                                                        <img src="https://cdn.tictacsante.com/assets_mail/icons/twitter-icon-mobile.png" alt="Twitter" border="0" width="30" height="30">
                                                                                    </a>
                                                                                </td>
                                                                                <td style="display:inline-block;padding:0 4px;text-align:center">
                                                                                    <a href="https://www.facebook.com/tictacsante" style="text-decoration:none" target="_blank">
                                                                                        <img src="https://cdn.tictacsante.com/assets_mail/icons/facebook-icon-mobile.png" alt="Facebook" border="0" width="30" height="30">
                                                                                    </a>
                                                                                </td>
                                                                                <td style="display:inline-block;padding:0 4px;text-align:center">
                                                                                    <a href="https://www.instagram.com/tictacsante" style="text-decoration:none" target="_blank">
                                                                                        <img src="https://cdn.tictacsante.com/assets_mail/icons/instagram-icon-mobile.png" alt="Instagram" border="0" width="30" height="30">
                                                                                    </a>
                                                                                </td>
                                                                                <td style="display:inline-block;padding:0 4px;text-align:center">
                                                                                    <a href="https://www.youtube.com/channel/UCCUW5IGOw1z0gfAWWWPLjJw" style="text-decoration:none" target="_blank">
                                                                                        <img src="https://cdn.tictacsante.com/assets_mail/icons/youtube-icon-mobile.png" alt="YouTube" border="0" width="30" height="30">
                                                                                    </a>
                                                                                </td>
                                                                                <td style="display:inline-block;padding:0 4px;text-align:center">
                                                                                    <a href="https://www.linkedin.com/company/tictacsante/" style="text-decoration:none" target="_blank">
                                                                                        <img src="https://cdn.tictacsante.com/assets_mail/icons/linkedin-icon-mobile.png" alt="LinkedIn" border="0" width="30" height="30">
                                                                                    </a>
                                                                                </td>
                                                                            </tr>

                                                                            </tbody>
                                                                        </table>
                                                                    </div>
                                                                    <div style="display:inline-block;padding:4px 0 0 12px;text-align:right;vertical-align:middle;width:217px">
                                                                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                            <tbody>
                                                                            <tr>
                                                                                <td height="5" width="5" style="font-size:0;line-height:0"></td>
                                                                            </tr>
                                                                            </tbody>
                                                                        </table>
                                                                        <div style="font-family:Arial,Helvetica,sans-serif,'Roboto';font-size:16px;letter-spacing:-0.5px;font-weight:bold;color:#888888;font-size:14px;font-weight:normal;text-align:center">
                                                                            Téléchargez notre application dès maintenant!
                                                                        </div>
                                                                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                            <tbody>
                                                                            <tr>
                                                                                <td height="5" width="5" style="font-size:0;line-height:0"></td>
                                                                            </tr>
                                                                            </tbody>
                                                                        </table>
                                                                        <table border="0" cellpadding="0" cellspacing="0" style="width:100%">
                                                                            <tbody>
                                                                            <tr>
                                                                                <td style="display:inline-block;padding:0 3px">
                                                                                    <a href="https://apps.tictacsante.com" target="_blank" >
                                                                                        <img src="https://cdn.tictacsante.com/assets_mail/_elements/google-play-2x.png" alt="Obtenir sur Google Play" border="0" width="97" height="30" style="height:auto;width:97px">
                                                                                    </a>
                                                                                </td>
                                                                                <td style="display:inline-block;padding:0 3px">
                                                                                    <a href="https://apps.tictacsante.com" style="text-decoration:none" target="_blank">
                                                                                        <img src="https://cdn.tictacsante.com/assets_mail/_elements/app-store-2x.png" alt="Télécharger sur App Store" border="0" width="97" height="30" style="height:auto;width:97px">
                                                                                    </a>
                                                                                </td>
                                                                            </tr>
                                                                            </tbody>
                                                                        </table>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                        <td width="16"></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <table bgcolor="#2085c6" border="0" cellpadding="0" cellspacing="0" style="background-color:#2085c6;margin:0 auto;width:100%; height: 80px">
                                    <tbody>
                                    <tr>
                                        <td>
                                            <table cellpadding="0" cellspacing="0" style="width:100%">
                                                <tbody>
                                                <tr>
                                                    <td style="padding:0">
                                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="background-color:#2085c6;color:#fff">
                                                            <tbody>
                                                            <tr>
                                                                <td valign="middle" style="padding:16px">
                                                                    <a href="https://www.tictacsante.com" style="color:#ffffff" target="_blank">
                                                                        <img src="https://cdn.tictacsante.com/assets/images/logo_w.png" alt="TicTacSanté" border="0" width="180">
                                                                    </a>
                                                                </td>
                                                                <td align="right" valign="middle" style="color:#fff;font-size:10px; ">

                                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style="background-color:#2085c6;color:#fff">
                                                                        <tbody>
                                                                        <tr>
                                                                            <td valign="middle" style="">
                                                                                <div style="width:130px;display:inline-block;text-align:left;font-size:15px;background:url(https://cdn.tictacsante.com/assets_mail/_elements/icontel.png) center left no-repeat;padding-left:45px;display:inline-block;font-family:sans-serif;font-weight:100;line-height:21px;">
                                                                                    <p style="font-size:14px;margin:0">Des questions ?</p>
                                                                                    <span style="font-size:13px">+212 (0) 537 690 620</span>
                                                                                </div>
                                                                            </td>
                                                                            <td align="right" valign="middle" style="">

                                                                                <div style="width:130px;text-align:left;font-size:15px;background:url(https://cdn.tictacsante.com/assets_mail/_elements/iconsupport.png) center left no-repeat;padding-left:45px;display:inline-block;font-family:sans-serif;font-weight:100;line-height:21px;">
                                                                                    <p style="font-size:14px;margin:0">Nous contacter</p>
                                                                                    <a style="color:#fff;font-size:13px" href="mailto:infos@tictacsante.com" target="_blank">infos@tictacsante.com</a>
                                                                                </div>
                                                                            </td>
                                                                        </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding-bottom:3px;background-color:#444444;color:#fff" align="center">
                                                        <a href="https://about.tictacsante.com/conditions-generales-utilisation" style="color:#fff;font-family:Arial,Helvetica,sans-serif,'Roboto';font-size:10px;text-decoration:none" target="_blank">
                                                            CGU
                                                        </a>
                                                        &nbsp;&nbsp;|&nbsp;&nbsp;
                                                        <a href="https://about.tictacsante.com/politique-confidentialite" style="color:#fff;font-family:Arial,Helvetica,sans-serif,'Roboto';font-size:10px;text-decoration:none" target="_blank">
                                                            Politique de confidentialité
                                                        </a>
                                                        &nbsp;&nbsp;|&nbsp;&nbsp;<a href="https://about.tictacsante.com/contact" style="color:#fff;font-family:Arial,Helvetica,sans-serif,'Roboto';font-size:10px;text-decoration:none" target="_blank">
                                                            Nous contacter</a>
                                                        &nbsp;&nbsp;|&nbsp;&nbsp;
                                                        <a href="https://www.tictacsante.com/se-desabonner" style="color:#fff;font-family:Arial,Helvetica,sans-serif,'Roboto';font-size:10px;text-decoration:none" target="_blank">
                                                            Se désinscrire
                                                        </a>
                                                    </td>

                                                </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <p style="margin:0;margin-top:20px;text-align:center;font-family:sans-serif;color:#5f5f5f;font-size:13px;font-weight:100">
                    TicTacSanté © 2014/${year} - Tous droits réservés.
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>


