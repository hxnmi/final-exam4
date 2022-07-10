package UAS;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Data_Buku {

	private JFrame frame;
	private JTextField txtjudulbuku;
	private JTextField txtpengarang;
	private JTextField txtpenerbit;
	private JTextField txttahunterbit;
	private DefaultTableModel model;
	private JComboBox status;
	Connection conn;
	Statement stat;
	ResultSet rs;

	final static String JAVA_DRIVER = "com.mysql.cj.jdbc.Driver";
	static String DB_URL = "jdbc:mysql://127.0.0.1/sewabuku";
	final static String USER = "root";
	final static String PASS = "";
	private JTable table;

	JButton btnSimpan = new JButton("Simpan");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Data_Buku window = new Data_Buku();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Data_Buku() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				resetForm();
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					Class.forName(JAVA_DRIVER);
					conn = DriverManager.getConnection(DB_URL, USER, PASS);
					showData();
				} catch (Exception err) {
					JOptionPane.showMessageDialog(null, "Error,cant connect to Database", "Connection Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		frame.setBounds(100, 100, 414, 421);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Data buku");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(20, 11, 63, 14);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Judul Buku");
		lblNewLabel_1.setBounds(20, 46, 88, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Pengarang");
		lblNewLabel_1_1.setBounds(20, 70, 88, 14);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_1 = new JLabel("Penerbit");
		lblNewLabel_1_1_1.setBounds(20, 94, 88, 14);
		frame.getContentPane().add(lblNewLabel_1_1_1);

		JLabel lblNewLabel_1_1_1_1 = new JLabel("Tahun Terbit");
		lblNewLabel_1_1_1_1.setBounds(20, 118, 88, 14);
		frame.getContentPane().add(lblNewLabel_1_1_1_1);

		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Status");
		lblNewLabel_1_1_1_1_1.setBounds(20, 142, 63, 14);
		frame.getContentPane().add(lblNewLabel_1_1_1_1_1);

		txtjudulbuku = new JTextField();
		txtjudulbuku.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtjudulbuku.setBounds(118, 46, 166, 20);
		frame.getContentPane().add(txtjudulbuku);
		txtjudulbuku.setColumns(10);

		txtpengarang = new JTextField();
		txtpengarang.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtpengarang.setColumns(10);
		txtpengarang.setBounds(118, 70, 166, 20);
		frame.getContentPane().add(txtpengarang);

		txtpenerbit = new JTextField();
		txtpenerbit.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtpenerbit.setColumns(10);
		txtpenerbit.setBounds(118, 94, 166, 20);
		frame.getContentPane().add(txtpenerbit);

		txttahunterbit = new JTextField();
		txttahunterbit.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txttahunterbit.setColumns(10);
		txttahunterbit.setBounds(118, 118, 166, 20);
		frame.getContentPane().add(txttahunterbit);

		status = new JComboBox();
		status.setFont(new Font("Tahoma", Font.PLAIN, 11));
		status.setModel(new DefaultComboBoxModel(new String[] { "Tersedia", "Tidak" }));
		status.setBounds(118, 142, 166, 20);
		frame.getContentPane().add(status);

		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String judul, pengarang, penerbit;
					Object stn;
					int tahunterbit, statt;
					judul = txtjudulbuku.getText();
					pengarang = txtpengarang.getText();
					penerbit = txtpenerbit.getText();
					tahunterbit = Integer.parseInt(txttahunterbit.getText());
					stn = status.getSelectedItem();

					if (stn == "Tersedia") {
						statt = 1;
					} else {
						statt = 0;
					}
					insert(judul, pengarang, penerbit, tahunterbit, statt);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Mohon isi kolom dengan benar", "Kesalahan Input",
							JOptionPane.ERROR_MESSAGE);
				}

			}

		});

		btnSimpan.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSimpan.setBounds(10, 193, 71, 23);
		frame.getContentPane().add(btnSimpan);

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editData();
			}
		});
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnEdit.setBounds(91, 193, 57, 23);
		frame.getContentPane().add(btnEdit);

		JButton btnHapus = new JButton("Hapus");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data?");

				try {
					if (response == 0) {
						if (table.getSelectedRow() >= 0) {
							String judul = table.getValueAt(table.getSelectedRow(), 0).toString();
							hapusData(judul);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Hapus data dibatalkan");
					}

					table.setModel(model);
					table.setAutoResizeMode(0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnHapus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnHapus.setBounds(158, 193, 71, 23);
		frame.getContentPane().add(btnHapus);

		JButton btnCetak = new JButton("Cetak Laporan");
		btnCetak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				try {
					Class.forName(JAVA_DRIVER);
					conn = DriverManager.getConnection(DB_URL, USER, PASS);
					String sql = "SELECT * FROM buku";

					JasperDesign jdesign = JRXmlLoader.load("res\\Data_Buku.jrxml");

					JRDesignQuery updateQuery = new JRDesignQuery();
					updateQuery.setText(sql);

					jdesign.setQuery(updateQuery);

					JasperReport Jreport = JasperCompileManager.compileReport(jdesign);
					JasperPrint JasperPrint = JasperFillManager.fillReport(Jreport, null, conn);
					JasperViewer.viewReport(JasperPrint, false);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2);
				}
			}
		});
		btnCetak.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCetak.setBounds(239, 193, 115, 23);
		frame.getContentPane().add(btnCetak);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 227, 378, 144);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String judul = table.getValueAt(table.getSelectedRow(), 0).toString();
				btnSimpan.setEnabled(false);
				getData(judul);
			}
		});
		scrollPane.setViewportView(table);

	}

	public void resetForm() {
		txtjudulbuku.setText("");
		txtpengarang.setText("");
		txtpenerbit.setText("");
		txttahunterbit.setText("");
		// status.setSelectedIndex(0);
		btnSimpan.setEnabled(true);
	}

	void insert(String judul, String pengarang, String penerbit, int tahun_terbit, int statt) {
		try {
			Class.forName(JAVA_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stat = conn.createStatement();
			String sqlin = "INSERT INTO buku (judul_buku, pengarang, penerbit, tahun_terbit, status) VALUES(?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sqlin);
			ps.setString(1, judul);
			ps.setString(2, pengarang);
			ps.setString(3, penerbit);
			ps.setInt(4, tahun_terbit);
			ps.setInt(5, statt);

			ps.execute();

			JOptionPane.showMessageDialog(new JFrame(), "Berhasil Berhasil Horaayyy!!!");

			stat.close();
			conn.close();

			showData();

		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Class Not Found", "Koneksi Gagal", JOptionPane.ERROR_MESSAGE);
			System.out.println(e);
		} catch (SQLIntegrityConstraintViolationException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Kode Barang sudah ada, mohon gunakan kode yang lain",
					"Koneksi Gagal", JOptionPane.ERROR_MESSAGE);
			System.out.println(e);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), "gagal terhubung ke database", "Koneksi Gagal",
					JOptionPane.ERROR_MESSAGE);
			System.out.println(e);
		}

	}

	public void showData() {
		model = new DefaultTableModel();

		model.addColumn("Judul Buku");
		model.addColumn("Pengarang");
		model.addColumn("Penerbit");
		model.addColumn("Tahun Terbit");
		model.addColumn("Status");

		try {
			Class.forName(JAVA_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stat = conn.createStatement();

			rs = stat.executeQuery("SELECT * FROM buku");
			while (rs.next()) {
				model.addRow(new Object[] { rs.getString("judul_buku"), rs.getString("pengarang"),
						rs.getString("penerbit"), rs.getString("tahun_terbit"), rs.getString("status") });
			}

			stat.close();
			conn.close();

			table.setModel(model);
			table.setAutoResizeMode(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hapusData(String judul) {
		try {
			Class.forName(JAVA_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stat = conn.createStatement();

			String sql = "DELETE FROM buku WHERE judul_buku=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, judul);

			ps.execute();

			stat.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		showData();
		resetForm();
	}

	public void getData(String judul) {
		try {
			Class.forName(JAVA_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stat = conn.createStatement();

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM buku WHERE judul_buku =?");
			ps.setString(1, judul);
			rs = ps.executeQuery();
			rs.next();

			txtjudulbuku.setText(rs.getString("judul_buku"));
			txtpengarang.setText(rs.getString("pengarang"));
			txttahunterbit.setText(rs.getString("tahun_terbit"));
			txtpenerbit.setText(rs.getString("penerbit"));
			status.setSelectedItem(Integer.parseInt(rs.getString("status")) == 1? "Tersedia" : "Tidak");

			stat.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void editData() {
		String kode = txtjudulbuku.getText();
		String c1 = txtpengarang.getText();
		int c2 = Integer.parseInt(txttahunterbit.getText()); 
		String c3 = txtpenerbit.getText();
		int c4 = (status.getSelectedItem() == "Tersedia"? 1: 0);

		try {
			Class.forName(JAVA_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stat = conn.createStatement();

			rs = stat.executeQuery("SELECT * FROM buku WHERE (judul_buku= '"+kode+"')");
			rs.next();
			
			int idBuku = Integer.parseInt(rs.getString("buku_id"));
			
			String sqledt = "UPDATE buku SET judul_buku=?, pengarang =?, tahun_terbit = ?, penerbit = ? , status=?  WHERE  buku_id=? ";
			PreparedStatement ps = conn.prepareStatement(sqledt);
			ps.setString(1, kode);
			ps.setString(2, c1);
			ps.setInt(3, c2);
			ps.setString(4, c3);
			ps.setInt(5, c4);
			ps.setInt(6, idBuku);

			ps.execute();

			JOptionPane.showMessageDialog(new JFrame(), "Berhasil update!!!");
			stat.close();
			conn.close();
			showData();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "Tidak ada yang diedit!", "Kesalahan Input",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
